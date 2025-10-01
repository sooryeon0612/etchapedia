package com.etchapedia;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.etchapedia.pay.OrderRequestDto;
import com.etchapedia.pay.OrderService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;

import jakarta.servlet.http.HttpSession;
//KakaoPayController.java
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order/pay")
@RequiredArgsConstructor
public class KakaoPayController {
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private OrderService oSvc;
	
	@Value("${kakao.pay.cid}")
    private String CID;

    @Value("${kakao.pay.admin-key}")
    private String ADMIN_KEY;

	private String tid; // 결제 고유번호 저장 (실제로는 DB에 저장 권장)

	
	// 작업자 : 이경미
	// 기능 : 카카오페이 결제 준비
	@PostMapping("/ready")
	@ResponseBody
	public Map<String, Object> kakaoPayReady(@RequestBody OrderRequestDto orderRequest,
	                                         @AuthenticationPrincipal UserDetails userDetails,
	                                         HttpSession session) {
	    Users user = uRepo.findByEmail(userDetails.getUsername()).orElseThrow();

	    RestTemplate restTemplate = new RestTemplate();

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
	    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("cid", CID);
	    params.add("partner_order_id", "ORDER-" + System.currentTimeMillis());
	    params.add("partner_user_id", user.getUserIdx().toString());
	    params.add("item_name", orderRequest.getName());
	    params.add("quantity", String.valueOf(orderRequest.getQuantities().size()));
	    params.add("total_amount", String.valueOf(orderRequest.getTotalPrice()));
	    params.add("tax_free_amount", "0");
	    params.add("approval_url", "http://localhost:9090/order/pay/success");
	    params.add("cancel_url", "http://localhost:9090/order/pay/cancel");
	    params.add("fail_url", "http://localhost:9090/order/pay/fail");

	    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

	    ResponseEntity<Map> response = restTemplate.postForEntity(
	            "https://kapi.kakao.com/v1/payment/ready",
	            requestEntity,
	            Map.class
	    );

	    Map<String, Object> result = response.getBody();
	    String tid = (String) result.get("tid");

	    // 세션에 저장
	    session.setAttribute("KAKAO_TID", tid);
	    session.setAttribute("ORDER_ID", params.getFirst("partner_order_id"));

	    return result;
	}

	// 작업자 : 이경미
	// 기능 : 카카오페이 최종 결제 승인
    @GetMapping("/success")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            String tid = (String) session.getAttribute("KAKAO_TID");
            String orderId = (String) session.getAttribute("ORDER_ID");
            String userId = session.getId();

            if (tid == null || orderId == null) {
                redirectAttributes.addFlashAttribute("payResult", "결제 정보가 존재하지 않습니다.");
                return "redirect:/order/pay/fail";
            }

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("cid", CID);
            params.add("tid", tid);
            params.add("partner_order_id", orderId);
            params.add("partner_user_id", userId);
            params.add("pg_token", pgToken);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://kapi.kakao.com/v1/payment/approve",
                    requestEntity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            redirectAttributes.addFlashAttribute("payResult", body);

            // 결제 완료 후 세션 정리
            session.removeAttribute("KAKAO_TID");
            session.removeAttribute("ORDER_ID");

            return "redirect:/order/pay/complete";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("payResult", "결제 승인 실패: " + e.getMessage());
            return "redirect:/order/pay/fail";
        }
    }
    
    // 작업자 : 이경미
    // 결제 취소 화면
    @GetMapping("/cancel")
    public String kakaoPayCancel() {
        return "order/pay/cancel";
    }

    // 작업자 : 이경미
    // 결제 실패 화면
    @GetMapping("/fail")
    public String kakaoPayFail() {
        return "order/pay/fail";
    }

    // 작업자 : 이경미
    // 결제 완료 화면
    @GetMapping("/complete")
    public String completePage() {
        return "order/pay/complete";
    }
}