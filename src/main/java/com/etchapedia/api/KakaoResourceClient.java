package com.etchapedia.api;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoResourceClient {
	private WebClient webClient;
	
	public KakaoResourceClient(WebClient.Builder builder) {
		this.webClient = builder
		    .baseUrl("https://kapi.kakao.com")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
	}
	
	public String callKakaoResource(String accessToken) {
		   return webClient.get()
				   	.uri("/v2/user/me")
		   			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
	                .retrieve()
	                .bodyToMono(String.class)
	                .block();
   }
}
