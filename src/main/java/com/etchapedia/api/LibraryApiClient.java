package com.etchapedia.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LibraryApiClient {
	private WebClient webClient;
	private String certKey = "d88072d3bf902e24b4198873b217d47d308076cc2d08fd3977859a952bf461ec";
	
	public LibraryApiClient(WebClient.Builder builder) {
		this.webClient = builder
		    .baseUrl("https://www.nl.go.kr") // 기본 도메인
		    .build();
	}

    public String callLibraryApi(int pageNo, int pageSize) {
    	return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/seoji/SearchApi.do")
                    .queryParam("cert_key", certKey)
                    .queryParam("result_style", "json")
                    .queryParam("form", "종이책")
                    .queryParam("page_no", pageNo)
                    .queryParam("page_size", pageSize)
                    .queryParam("ebook_yn", "N")
                    .queryParam("deposit_yn", "Y")
                    .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    

}
