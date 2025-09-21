package com.etchapedia.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
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
	private ChatGptApiClient gpt;
	@Autowired
	private WikiApiClient wiki;
	
	@Autowired
	private BookRepository bRepo;

	
	// 정보나루 인기대출도서 api 호출 후 book 객체 리턴 
	public List<Book> loadBooksFromLibrary(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<Book> bookList = new ArrayList<>();
		String rawBooks = lib.callLibraryApi(pageNo, pageSize);
		
		JsonNode root = mapper.readTree(rawBooks);
		JsonNode docs = root.path("response").path("docs");
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

			bookList.add(b);
		}
		return bookList;
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
				
				if(isbn.equals("") || title.equals("")) continue;
				
				Book b = new Book();
				b.setTitle(title);
				b.setAuthor(author);
				b.setIsbn(isbn);
				
				bookList.add(b);
			}
		}
		return bookList;
	}

	// 대출수 채운 뒤 book 객체 리턴 
	public Book getLoanByIsbn(Book book) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rawInfo = lib.callSrchDtlListApi(book.getIsbn());
		JsonNode root = mapper.readTree(rawInfo);
		String loanStr = root.path("response").path("loanInfo").path(0).path("Total").path("loanCnt").asText();
		book.setLoan(Integer.parseInt(loanStr.equals("") ? "0" : loanStr));
		return book;
	}
	
	// 네이버 api 호출 후 필요한 정보만 채운 뒤 book객체 리턴 
	public Book getBookInfoFromNaver(Book book) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String info = naver.callNaverApiByIsbn(book.getIsbn());
		JsonNode root = mapper.readTree(info);
		
		// 조회한 정보가 없거나 설명, 가격이 없다면 isbn을 -1로 바꿈 
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
	
	// 네이버에서 키워드로 책 검색
	public List<Book> findBooksByKeywordFromNaver(String keyword) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String result = naver.callNaverApiByKeyword(keyword);
		JsonNode items = mapper.readTree(result).path("items");
		List<Book> retList = new ArrayList<>();
		
		if(items.size() == 0) return retList;
		
		for(JsonNode item : items) {
			String description = item.path("description").asText();	
			String pic = item.path("image").asText();
			String price = item.path("discount").asText();
			String author = item.path("author").asText();
			String isbn = item.path("isbn").asText();
			String title = item.path("title").asText();
			
			if(description.equals("") || price.equals("") || pic.equals("") || author.equals("") || isbn.equals("") || title.equals("")) continue;
			
			Book book = new Book();
			book.setDescription(description);
			book.setPic(pic);
			book.setPrice(Integer.parseInt(price));
			book.setAuthor(author);
			book.setIsbn(isbn);
			book.setTitle(title);
			retList.add(book);
		}
		return retList;
	}
	
	// gpt 추천 책 가져오기 
	public List<Book> getGptRecommendBooks(List<Book> list) throws JsonMappingException, JsonProcessingException {
		List<Book> retList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(gpt.callGptApi(list));
		String content = root.path("choices").path(0).path("message").path("content").asText();
		JsonNode recommendations = mapper.readTree(content).path("recommendations");
		for(JsonNode book : recommendations) {
			Book b = new Book();
			b.setTitle(book.path("title").asText());
			retList.add(b);
		}
		return retList;
	}
	
	// 제목으로 네이버에서 책 한 권 검색 
	public Book findBookByTitleFromNaver(Book book) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String info = naver.callNaverApiByTitle(book.getTitle());
		JsonNode root = mapper.readTree(info);
		
		if(root.path("items").size() == 0) {
			book.setIsbn("-1");
			return book;
		} else {
			JsonNode item = root.path("items").path(0);
			String description = item.path("description").asText();	
			String pic = item.path("image").asText();
			String price = item.path("discount").asText();
			String author = item.path("author").asText();
			String isbn = item.path("isbn").asText();
			String title = item.path("title").asText();

			if(!title.contains(book.getTitle()) || description.equals("") || price.equals("")) {
				book.setIsbn("-1");
				return book; 
			}
			book.setDescription(description);
			book.setPic(pic);
			book.setPrice(Integer.parseInt(price));
			book.setAuthor(author);
			book.setIsbn(isbn);
			return book;
		}
	}
	
	// 위키 api로 질문에 답하기
	public String getAnswerFromWiki(String question) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String responseBody = wiki.callWikiApi(question);
		JsonNode root = mapper.readTree(responseBody);
		JsonNode answerNode = root.path("return_object").path("WiKiInfo").path("AnswerInfo").path(0).path("answer");
		return answerNode.asText();
	}
	
	
	
}
