package com.etchapedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.pay.Cart;
import com.etchapedia.pay.CartService;
import com.etchapedia.pay.OrderRequestDto;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;
import com.etchapedia.user.UsersService;

@Controller
public class CartController {
	@Autowired
	private CartService cSvc;
	@Autowired
	private UsersService uSvc;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private UsersRepository uRepo;
    
	// 작업자 : 이경미 
    // 기능 : 장바구니에 상품 추가
    @PostMapping("/cart/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Integer> payload,
    													 @AuthenticationPrincipal UserDetails userDetails) {
        Integer bookIdx = payload.get("bookIdx");
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 책 정보 조회
            Book book = bRepo.findById(bookIdx)
                                      .orElseThrow(() -> new IllegalArgumentException("해당하는 책을 찾을 수 없습니다."));

            // 2. 현재 로그인된 사용자 정보를 가져옵니다.
            // Spring Security를 통해 얻은 userDetails 객체에서 사용자 이름(username)을 가져옵니다.
            String email = userDetails.getUsername(); 
            Users user = uRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 3. 장바구니에 책과 사용자 정보를 함께 추가
            cSvc.addBookToCart(book, user);

            // 4. 성공 응답
            response.put("success", true);
            response.put("message", "책이 장바구니에 성공적으로 추가되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 5. 책을 찾지 못했을 때 실패 응답
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 6. 그 외 다른 오류가 발생했을 때 실패 응답
            response.put("success", false);
            response.put("message", "장바구니 담기 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 작업자 : 이경미
    // 기능 : 장바구니 수량 변경
    @PostMapping("/cart/update-quantity")
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestBody Map<String, Object> payload,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        // 1. 요청 받은 bookId와 변경 값(change) 가져오기
        Integer bookIdx = (Integer) payload.get("bookIdx");
        Integer change = (Integer) payload.get("change"); // change는 1 또는 -1

        try {
            // 2. 사용자 정보와 책 정보로 장바구니 아이템 찾기
            Users user = uRepo.findByEmail(userDetails.getUsername())
                               .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            Book book = bRepo.findById(bookIdx)
                               .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));

            // 3. CartService의 업데이트 로직 호출
            cSvc.updateCartItemQuantity(user, book, change);

            // 4. 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 5. 실패 응답
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 작업자 : 이경미
    // 기능 : 장바구니 전체 정보 가져오기
    @GetMapping("/cart")
    public String showCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    	// 1. 템플릿에 전달할 변수들을 기본값으로 초기화
        List<Cart> cartItems = new ArrayList<>();
        Long totalPrice = 0L;
        String cartSummaryName = "장바구니 상품"; // 기본값 설정

        // 2. 사용자가 로그인했는지 확인
        if (userDetails != null) {
            String email = userDetails.getUsername(); 
            Optional<Users> userOpt = uRepo.findByEmail(email);

            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                
                // 3. CartService를 통해 장바구니 목록과 총 금액을 가져옴
                // 이 메서드는 직접 구현해야 함 (예시 코드)
                cartItems = cSvc.getCartItemsByUser(user);
                totalPrice = cSvc.calculateTotalPrice(cartItems);

                // 4. 결제 요약에 표시할 상품명 설정
                if (!cartItems.isEmpty()) {
                    String firstItemName = cartItems.get(0).getBook().getTitle();
                    cartSummaryName = (cartItems.size() > 1) ? firstItemName + " 외 " + (cartItems.size() - 1) + "건" : firstItemName;
                }
            }
        }
        
        // 5. 모든 변수를 모델에 담아 템플릿으로 전달
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartSummaryName", cartSummaryName);
        
        return "cart";
    }
    
    // 작업자 : 이경미
    // 주문 버튼 클릭 시 카카오페이 준비 요청
    @PostMapping("/cart/order")
    public ResponseEntity<Map<String, Object>> orderCart(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        Users user = uRepo.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            response.put("success", false);
            response.put("message", "사용자를 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        List<Cart> cartItems = cSvc.getCartItemsByUser(user);
        if (cartItems.isEmpty()) {
            response.put("success", false);
            response.put("message", "장바구니가 비어있습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        OrderRequestDto orderRequest = new OrderRequestDto();
        orderRequest.setTotalPrice(cSvc.calculateTotalPrice(cartItems));
        List<OrderRequestDto.ItemQuantity> quantities = new ArrayList<>();
        for (Cart c : cartItems) {
            OrderRequestDto.ItemQuantity iq = new OrderRequestDto.ItemQuantity();
            iq.setBookIdx(c.getBook().getBookIdx());
            iq.setQuantity(c.getQuantity());
            quantities.add(iq);
        }
        orderRequest.setQuantities(quantities);
        String firstItemName = cartItems.get(0).getBook().getTitle();
        orderRequest.setName(cartItems.size() > 1 ? firstItemName + " 외 " + (cartItems.size()-1) + "건" : firstItemName);

        response.put("success", true);
        response.put("orderRequest", orderRequest);
        return ResponseEntity.ok(response);
    }
}
