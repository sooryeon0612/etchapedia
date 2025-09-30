package com.etchapedia;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.book.ClickService;
import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsRepository;
import com.etchapedia.pay.CartService;
import com.etchapedia.security.CustomUserDetails;
import com.etchapedia.user.HateService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;

@Controller
public class BookController {
    @Autowired
    private BookRepository bRepo;
    @Autowired
    private CommentsRepository cmRepo;
    @Autowired
    private UsersRepository uRepo;
    @Autowired
    private CartService csSvc;
    @Autowired
    private HateService hSvc;
	@Autowired
    private ClickService clSvc;

	// 작업자 : 이경미
	// 기능 : 관심없어요 추가 / 삭제
    @PostMapping("/book/disinterest")
    @ResponseBody
    public Map<String, Object> hateBook(@RequestBody Map<String, Object> payload,
                                                 @AuthenticationPrincipal UserDetails user) {
        Integer bookIdx = Integer.valueOf(payload.get("bookId").toString());
        boolean isHated = Boolean.parseBoolean(payload.get("isHated").toString());

        Users loginUser = uRepo.findByEmail(user.getUsername())
                               .orElseThrow(() -> new RuntimeException("User not found"));

        boolean newState;
        if (isHated) {
            hSvc.addDisinterest(loginUser.getUserIdx(), bookIdx);
            newState = true;
        } else {
            hSvc.removeDisinterest(loginUser.getUserIdx(), bookIdx);
            newState = false;
        }

        return Map.of("bookIdx", bookIdx, "isHated", isHated);
    }


    
    // 작업자 : 이경미
	// 기능 : 특정 책에 달린 전체 정보 가져오기
    @GetMapping("/detail/page")
    public String showBookDetail(@RequestParam("id") Integer bookIdx, Model model,
                                 Principal principal,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Book book = bRepo.findById(bookIdx).orElse(null);
        model.addAttribute("book", book);

        List<Comments> comments = cmRepo.findByBook_BookIdx(bookIdx);
        model.addAttribute("comments", comments);

        // 로그인한 사용자 장바구니 개수 조회
        int cartCount = 0;
        if (principal != null) {
            Users user = uRepo.findByEmail(principal.getName()) 
                              .orElse(null);
            if (user != null) {
                cartCount = csSvc.getCartItemCount(user);
            }
        }
        model.addAttribute("cartCount", cartCount);

        if(userDetails != null)
        	clSvc.saveClickedBook(bookIdx, userDetails.getUserIdx());

        return "detail_page";
    }

}