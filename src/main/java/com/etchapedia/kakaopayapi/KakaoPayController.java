package com.etchapedia.kakaopayapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
  
    @GetMapping("/test")
    public String test() {
        // templates 디렉토리 내의 orderform.html을 찾으려면
        // 'orderform'만 반환하면 됩니다.
        return "orderform"; 
    }
   
    @PostMapping("/pay/ready")
    @ResponseBody
    public ReadyResponseDto payReady(@RequestBody OrderDto order) {      
        String name = order.getName();
        int totalPrice = order.getTotalPrice();      
        log.info("주문 상품 이름: " + name);
        log.info("주문 금액: " + totalPrice);

        // 카카오 결제 준비하기
        ReadyResponseDto readyResponse = kakaoPayService.payReady(name, totalPrice);
        // 세션에 결제 고유번호(tid) 저장
        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: " + readyResponse.getTid());
        return readyResponse;
    }

    @PostMapping("/pay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken) {
        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        ApproveResponseDto approveResponse = kakaoPayService.payApprove(tid, pgToken);
        return "order/paycompleted";
    }
    
    // 결제 취소 페이지
    @GetMapping("/pay/cancel")
    public String payCancel() {
        return "order/payCancel"; 
    }

    // 결제 실패 페이지
    @GetMapping("/pay/fail")
    public String payFail() {
        return "order/payFail"; 
    }
    
    
    
    
    
    
    
    
    @GetMapping("/cart")
    public String showCart(Model model) {
        // 테스트용 더미 데이터
        List<CartItemDto> cartItems = new ArrayList<>();
        cartItems.add(new CartItemDto("샘플상품1", 10000, 1, "/images/sample1.jpg", "무료배송"));
        cartItems.add(new CartItemDto("샘플상품2", 20000, 2, "/images/sample2.jpg", "무료배송"));

        int totalPrice = cartItems.stream().mapToInt(i -> i.getTotalPrice() * i.getQuantity()).sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartSummaryName", cartItems.get(0).getName() + " 외 " + (cartItems.size() - 1) + "개");

        return "cart"; // templates/cart.html
    }



}