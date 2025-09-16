package com.etchapedia.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etchapedia.home.Book;
import com.etchapedia.home.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookApiUtil {
	@Autowired
	private LibraryApiClient lib;
	@Autowired
	private NaverApiClient naver;
	@Autowired
	private BookRepository bRepo;

	// 정보나루 인기대출도서 api 호출 후 book 객체 리턴 
	public List<Book> loadBookFromApi(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<Book> bookList = new ArrayList<>();
		String rawBooks = lib.callLibraryApi(pageNo, pageSize);
		
		JsonNode root = mapper.readTree(rawBooks);
		JsonNode docs = root.path("response").path("docs");
		int save = 0;
		for(JsonNode item : docs) {
			JsonNode book = item.path("doc");
			String title = book.path("bookname").asText();
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
			
			// 네이버 api 에서 정보가 없을 경우 isbn을 -1로 바꿈 
			if(b.getIsbn().equals("-1")) continue;
			bookList.add(b);
		}
		return bookList;
    }
	
	// 네이버 api 호출 후 필요한 정보만 채운 뒤 book객체 리턴 
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
	
	// 정보나루 대출 급상승 도서 api 호출
	public List<Book> loadHotTrendBookList(String searchDt) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<Book> bookList = new ArrayList<>();
		String books = lib.callHotTrendApi(searchDt);
		
		JsonNode root = mapper.readTree(books);
		JsonNode results = root.path("response").path("results");
		for(JsonNode result : results) {
			JsonNode docs = result.path("result").path("docs");
			for(JsonNode doc : docs) {
				doc = doc.path("doc");
				String title = doc.path("bookname").asText();
				String author = doc.path("authors").asText();
				String isbn = doc.path("isbn13").asText();
				
				if(isbn.equals("")) continue;
				
				Book b = new Book();
				b.setTitle(title);
				b.setAuthor(author);
				b.setIsbn(isbn);
				
				b = naverBook(b);
				b = getLoanByIsbn(b);
				
				// 네이버 api 에서 정보가 없을 경우 isbn을 -1로 바꿈 
				if(b.getIsbn().equals("-1")) continue;
				bookList.add(b);
			}
		}
		return bookList;
	}
	
	public Book getLoanByIsbn(Book book) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rawInfo = lib.callSrchDtlListApi(book.getIsbn());
		JsonNode root = mapper.readTree(rawInfo);
		String loanStr = root.path("response").path("loanInfo").path(0).path("Total").path("loanCnt").asText();
		book.setLoan(Integer.parseInt(loanStr.equals("") ? "0" : loanStr));
		return book;
	}
	
	
	
}
