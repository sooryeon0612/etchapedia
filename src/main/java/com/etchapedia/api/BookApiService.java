package com.etchapedia.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookApiService {
	@Autowired
	private LibraryApiClient client;

	public String saveBooks(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String rawBooks = client.callLibraryApi(pageNo, pageSize);
		
		JsonNode root = mapper.readTree(rawBooks);
		JsonNode docs = root.path("docs");
		
		
		return docs.path(0).path("TITLE").asText();
    }
}
