package com.etchapedia.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoAuthClient {
	private WebClient webClient;

	@Value("${kakao.rest-api-key}")
	private String key;
	
	@Value("${kakao.redirect-uri}")
	private String redirectUri;
	
	public KakaoAuthClient(WebClient.Builder builder) {
		this.webClient = builder
		    .baseUrl("https://kauth.kakao.com")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
	}
	
	public String getKakaoAuthUrl() {
		return "https://kauth.kakao.com/oauth/authorize"
	             + "?response_type=code"
	             + "&client_id=" + key 
	             + "&redirect_uri=" + redirectUri;
	}

    public String getRawAccessToken(String code) {
    	return webClient.post()
    			.uri("/oauth/token")
				.body(BodyInserters.fromFormData("grant_type", "authorization_code")
			                       .with("client_id", key)
			                       .with("redirect_uri", redirectUri) 
			                       .with("code", code))
	            .retrieve()
	            .bodyToMono(String.class)
	            .block();
    }
}
