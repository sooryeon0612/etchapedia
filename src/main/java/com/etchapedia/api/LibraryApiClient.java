package com.etchapedia.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LibraryApiClient {
	private WebClient webClient;

	@Value("${library.api-key}")
	private String key;
	
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
    
    public String callHotTrendApi(String searchDt) {
    	return webClient.get()
    			.uri(uriBuilder -> uriBuilder
    					.path("/api/hotTrend")
                        .queryParam("authKey", key)
                        .queryParam("searchDt", searchDt)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    
    public String callSrchDtlListApi(String isbn) {
    	return webClient.get()
    			.uri(uriBuilder -> uriBuilder
    					.path("/api/srchDtlList")
                        .queryParam("authKey", key)
                        .queryParam("isbn13", isbn)
                        .queryParam("loaninfoYN", "Y")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    
    public String callSrchBooksListApi(String search) {
    	return webClient.get()
    			.uri(uriBuilder -> uriBuilder
    					.path("/api/srchBooks")
    					.queryParam("keyword", search)
    					.queryParam("pageNo", 1)
    					.queryParam("pageSize", 10)
    					.queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    

}
