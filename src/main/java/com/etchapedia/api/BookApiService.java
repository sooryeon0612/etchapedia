package com.etchapedia.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.home.Book;
import com.etchapedia.home.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookApiService {
	@Autowired
	private LibraryApiClient lib;
	@Autowired
	private NaverApiClient naver;
	@Autowired
	private BookRepository bRepo;

	public void saveBooks(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rawBooks = lib.callLibraryApi(pageNo, pageSize);
		
		JsonNode root = mapper.readTree(rawBooks);
		JsonNode docs = root.path("docs");
		
		int save = 0;
		for(JsonNode book : docs) {
			String isbn = book.path("EA_ISBN").asText();
			
			if(isbn.equals("")) continue;
			if(bRepo.findByIsbn(isbn).isPresent()) continue;
			
			Book b = new Book();
			b.setIsbn(book.path("EA_ISBN").asText());
			naverBook(isbn, b);
			
			if(b.getTitle() == null) {
				System.out.println("저장실패");
				continue;
			}
			bRepo.save(b);
			save++;
		}
		System.out.println("saved : " + save);
    }
	
	public Book naverBook(String isbn, Book rawBook) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String info = naver.callNaverApi(isbn);
		JsonNode root = mapper.readTree(info);
		
		if(root.path("items").size() == 0) {
			System.out.println("일치하는 책이 없습니다!");
			return rawBook;
		} else {
			JsonNode book = root.path("items").path(0);
			
			if(Integer.parseInt(book.path("discount").asText()) == 0) return rawBook; 
			
			rawBook.setTitle(book.path("title").asText());
			rawBook.setAuthor(book.path("author").asText());
			rawBook.setPic(book.path("image").asText());
			rawBook.setPrice(Integer.parseInt(book.path("discount").asText()));
			rawBook.setDescription(book.path("description").asText());
			return rawBook;
		}
			
		
	}
	
	
}
