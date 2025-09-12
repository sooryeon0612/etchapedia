package com.etchapedia.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NaverApiClient {
	private WebClient webClient;
	private String clientId = "JuMYHP4w2burQTipw_YW";
	private String clientSecret = "mXsX4ikakY";
	
	public NaverApiClient(WebClient.Builder builder) {
		this.webClient = builder
		    .baseUrl("https://openapi.naver.com") // 기본 도메인
		    .defaultHeader("X-Naver-Client-Id", clientId)
            .defaultHeader("X-Naver-Client-Secret", clientSecret)
		    .build();
	}
	
	public String callNaverApi(String isbn) {
    	return webClient.get()
            .uri(uriBuilder -> uriBuilder
            		.path("/v1/search/book_adv.json")
            		.queryParam("d_isbn", isbn)
            		.queryParam("sort", "date")
                    .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
