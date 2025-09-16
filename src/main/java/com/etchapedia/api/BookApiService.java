package com.etchapedia.api;

import java.util.Optional;

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
		JsonNode docs = root.path("response").path("docs");
		
		int save = 0;
		for(JsonNode item : docs) {
			JsonNode book = item.path("doc");
			String title = book.path("bookname").asText();
			
			String[] bad = {":", "=", "/"};
			for(int i=0; i<bad.length; i++) {
				int cut = title.indexOf(bad[i]);
				if(cut != -1) title = title.substring(0, title.indexOf(bad[i]));
			}
			
			String author = book.path("authors").asText();
			String isbn = book.path("isbn13").asText();
			int loan = Integer.parseInt(book.path("loan_count").asText());
			
			if(isbn.equals("")) continue;
			Optional<Book> om = bRepo.findByIsbn(isbn);
			if(om.isPresent()) continue;
			
			Book b = new Book();
			b.setTitle(title);
			b.setAuthor(author);
			b.setIsbn(isbn);
			b.setLoan(loan);
			
			b = naverBook(b);
			
			if(b.getIsbn().equals("-1")) continue;
			bRepo.save(b);
			save++;
		}
		System.out.println("saved : " + save);
    }
	
	public Book naverBook(Book book) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String info = naver.callNaverApi(book.getIsbn());
		JsonNode root = mapper.readTree(info);
		
		if(root.path("items").size() == 0) {
			book.setIsbn("-1");
			return book;
		} else {
			JsonNode item = root.path("items").path(0);
			String description = item.path("description").asText();	
			String pic = item.path("image").asText();
			String price = item.path("discount").asText();
			
			if(description.equals("") || price.equals("")) {
				book.setIsbn("-1");
				return book; 
			}
			book.setDescription(description);
			book.setPic(pic);
			book.setPrice(Integer.parseInt(price));
			return book;
		}
	}
	
}
