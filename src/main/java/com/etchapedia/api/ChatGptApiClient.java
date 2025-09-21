package com.etchapedia.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.etchapedia.book.Book;


@Component
public class ChatGptApiClient {
	private WebClient webClient;
	
	public ChatGptApiClient(WebClient.Builder builder, @Value("${gpt.api-key}")String key) {
		this.webClient = builder
			    .baseUrl("https://api.openai.com")
	            .defaultHeader("Authorization", "Bearer " + key)
	            .defaultHeader("Content-Type", "application/json")
	            .build();
	}
	
	public String callGptApi(List<Book> list) {
		String system = """
				SYSTEM
				당신은 사용자의 취향을 분석해 도서를 추천하는 전문가다.
				실존하지 않는 책은 추천하지 않는다.
				
				USER
				다음은 사용자가 읽거나 클릭한 책 목록이다. (제목·저자만 제공)
				이 목록의 장르/주제/분위기를 분석해, 겹치지 않는 실제 도서 10권을 추천하라.
				
				""";
		String seed = "";
		for(Book b : list) {
			seed += "{제목 : \"" + b.getTitle() + "\", 저자 : \"" + b.getAuthor() + "\"}\n";
		}
		String user = """
				[출력 규칙]
		        1. 정확히 10개의 도서를 추천한다.
		        2. 입력 목록과 동일한 제목은 포함하지 않는다. (대소문자, 공백 차이 무시)
		        3. JSON 객체는 "recommendations" 키 하나만 갖고, 값은 도서 배열이다.
		        4. 각 도서 객체는 "title" 키만 가진다.
		        5. 설명, 서론, 코드펜스, 기타 텍스트는 절대 포함하지 않는다.
				
				**원하는 JSON 형식 예시:**
				{"recommendations":[{"title":"소년이 온다"},{"title":"82년생 김지영"}, ... ]}
				""";
		
		Map<String, Object> requestBody = Map.of(
				"model", "gpt-4o-mini",
				"messages", List.of(
						Map.of("role", "user",
								"content", system + seed + user
						)
				),
				"temperature", 0.2,
				"response_format", Map.of("type", "json_object")
		);
		
    	return webClient.post()
    			.uri("/v1/chat/completions")
	            .bodyValue(requestBody)
	            .retrieve()
	            .bodyToMono(String.class)
	            .block();
    }
}
