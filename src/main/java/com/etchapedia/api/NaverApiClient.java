package com.etchapedia.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverApiClient {
	private WebClient webClient;
	
	public NaverApiClient(WebClient.Builder builder,
						@Value("${naver.client-id}")String clientId,
						@Value("${naver.client-secret}")String clientSecret) {
		this.webClient = builder
			    .baseUrl("https://openapi.naver.com")
			    .defaultHeader("X-Naver-Client-Id", clientId)
		        .defaultHeader("X-Naver-Client-Secret", clientSecret)
			    .build();
	}
	
	public String callNaverApiByIsbn(String isbn) {
    	return webClient.get()
            .uri(uriBuilder -> uriBuilder
            		.path("/v1/search/book_adv.json")
            		.queryParam("d_isbn", isbn)
            		.queryParam("sort", "date")
            		.queryParam("display", 1)
                    .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
	
	public String callNaverApiByTitle(String title) {
    	return webClient.get()
            .uri(uriBuilder -> uriBuilder
            		.path("/v1/search/book_adv.json")
            		.queryParam("d_titl", title)
            		.queryParam("sort", "sim")
            		.queryParam("display", 1)
                    .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
