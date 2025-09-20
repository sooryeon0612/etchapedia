package com.etchapedia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.etchapedia.book.GptRecommendationsService;
import com.etchapedia.home.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/ajax")
public class RestController {
	@Autowired
	private GptRecommendationsService gSvc;
	
	@PostMapping("load_recommend_books")
	public List<Book> loadRecommendBook(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		LocalDate lastUpdate = gSvc.getLastUpdateDate(userIdx);
    	if(!lastUpdate.equals(LocalDate.now())) {
    		List<Book> recommedList = gSvc.getGptRecommendBooks(userIdx);
    		gSvc.logRecommendation(recommedList, userIdx);
    	}
    	return gSvc.getRecommendedBooks(1);
	}
}
