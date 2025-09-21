package com.etchapedia.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WikiApiClient {
private WebClient webClient;
	
	public WikiApiClient(WebClient.Builder builder,
						@Value("${wiki.api-key}")String key) {
		this.webClient = builder
			    .baseUrl("http://epretx.etri.re.kr:8000")
			    .defaultHeader("Authorization", key)
			    .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
			    .build();
	}
	
	public String callWikiApi(String question) {
		Map<String, Object> requestBody = Map.of(
				"request_id", "reserved field",
				"argument", Map.of(
						"passage", "책에 대한 질문",
						"question", question,
						"type", "ENGINE_TYPE")
		);
		
		return webClient.post()
    			.uri("/api/WikiQA")
	            .bodyValue(requestBody)
	            .retrieve()
	            .bodyToMono(String.class)
	            .block();
    }

}
