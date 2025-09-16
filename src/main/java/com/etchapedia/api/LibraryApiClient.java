package com.etchapedia.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LibraryApiClient {
	private WebClient webClient;
	private String key = "933007855a408647352fc853a7bc2fc40ee299bf420d1a503775bfcecc7ef159";
	
	public LibraryApiClient(WebClient.Builder builder) {
		this.webClient = builder
		    .baseUrl("http://data4library.kr") // 기본 도메인
		    .build();
	}

    public String callLibraryApi(int pageNo, int pageSize) {
    	return webClient.get()
    			.uri(uriBuilder -> uriBuilder
                        .path("/api/loanItemSrch")
                        .queryParam("authKey", key)
                        .queryParam("startDt", "2024-01-01")
                        .queryParam("format", "json")
                        .queryParam("pageNo", pageNo)
                        .queryParam("pageSize", pageSize)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    

}
