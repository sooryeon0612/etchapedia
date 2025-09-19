package com.etchapedia.kakaopayapi;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoPayService {

    // 카카오페이 결제창 연결
    public ReadyResponseDto payReady(String name, int totalPrice) {
    
    	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.add("partner_order_id", "1234567890");                       // 주문번호
        parameters.add("partner_user_id", "roommake");                          // 회원 아이디
        parameters.add("item_name", name);                                      // 상품명
        parameters.add("quantity", "1");                                        // 상품 수량
        parameters.add("total_amount", String.valueOf(totalPrice));             // 상품 총액
        parameters.add("tax_free_amount", "0");                                 // 상품 비과세 금액
        parameters.add("approval_url", "http://localhost:9090/order/pay/completed"); // 결제 성공 시 URL
        parameters.add("cancel_url", "http://localhost:9090/order/pay/cancel");      // 결제 취소 시 URL
        parameters.add("fail_url", "http://localhost:9090/order/pay/fail");          // 결제 실패 시 URL

        log.info("approval_url = {}", parameters.getFirst("approval_url"));
        log.info("cancel_url   = {}", parameters.getFirst("cancel_url"));
        log.info("fail_url     = {}", parameters.getFirst("fail_url"));
        
        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
//      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        
        // RestTemplate
        // Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        // REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponseDto> responseEntity = template.postForEntity(url, requestEntity, ReadyResponseDto.class);
        log.info("결제준비 응답객체: " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    // 카카오페이 결제 승인 (최종 결제 완료 단계)
    public ApproveResponseDto payApprove(String tid, String pgToken) {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("cid", "TC0ONETIME");             
//        parameters.put("tid", tid);                       
//        parameters.put("partner_order_id", "1234567890");
//        parameters.put("partner_user_id", "roommake");    
//        parameters.put("pg_token", pgToken);              

//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

    	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("cid", "TC0ONETIME");		 // 가맹점 코드(테스트용)
    	parameters.add("tid", tid);						// 결제 고유번호
    	parameters.add("partner_order_id", "1234567890");  // 주문번호
    	parameters.add("partner_user_id", "roommake"); // 회원 아이디
    	parameters.add("pg_token", pgToken); // 결제승인 요청을 인증하는 토큰

    	HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/approve";
//        ApproveResponseDto approveResponse = template.postForObject(url, requestEntity, ApproveResponseDto.class);
//        log.info("결제승인 응답객체: " + approveResponse);
//
//        return approveResponse;
        return template.postForObject(url, requestEntity, ApproveResponseDto.class);
    }
    
    // 카카오페이 측에 요청 시 헤더부에 필요한 값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "SECRET_KEY " + "DEV8C38994C6115FF188895CE410DD5A9FAF6B7B");
//        headers.set("Content-type", "application/json");
//
//        return headers;
        
        headers.add("Authorization", "SECRET_KEY " + "112a08020ed654fe19afa6e56ad0a817"); // REST API KEY
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        return headers;
    }
}
