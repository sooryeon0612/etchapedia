package com.etchapedia.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.etchapedia.api.BookApiUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class BookService {
    private final PasswordEncoder pwEncoder;
    
	@Autowired
	private BookApiUtil util;
	@Autowired
	private BookRepository bRepo;

    BookService(PasswordEncoder pwEncoder) {
        this.pwEncoder = pwEncoder;
    }
    
    
	
	public void saveBooks(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		List<Book> bookList = util.loadBooksFromLibrary(pageNo, pageSize);
		int save = 0;
		
		for(Book b : bookList) {
			b = util.getBookInfoFromNaver(b);
			if(b.getIsbn().equals("-1")) continue;
			if(cleanUpTitle(b) == null) continue;
			b = cleanUpTitle(b);
			bRepo.save(b);
			save++;
		}
		System.out.println("saved : " + save);
	}
	
	public List<Book> getPopularBooks() {
		return bRepo.findTop10ByOrderByLoanDesc();
	}
	
	public List<Book> getSearchBooks(String search) {
		List<Book> retList = new ArrayList<>();
		retList.addAll(bRepo.findByTitleContaining(search));
		retList.addAll(bRepo.findByAuthorContaining(search));
		return retList;
	}
	
	public void saveNewBooksFromKeyword(String keyword) throws JsonMappingException, JsonProcessingException {
		List<Book> list = util.findBooksByKeywordFromNaver(keyword);
		for(Book b : list) {
			Optional<Book> ob = bRepo.findByIsbn(b.getIsbn());
			if(ob.isPresent()) continue;
			if(cleanUpTitle(b) == null) continue;
			b = cleanUpTitle(b);
			b = util.getLoanByIsbn(b);
			bRepo.save(b);
		}
	}
	
	public Book cleanUpTitle(Book b) {
		String[] bad = {":", "=", "/", "(", ")"};
		String title = b.getTitle();
		for(int i=0; i<bad.length; i++) {
			int cut = title.indexOf(bad[i]);
			if(cut != -1) {
				title = title.substring(0, title.indexOf(bad[i]));
				if(title.equals("")) return null;
				b.setTitle(title);
			}
		}
		return b;
	}
	
	public String searchDictionary(String search) throws JsonMappingException, JsonProcessingException {
		return util.getAnswerFromWiki(search);
	}
	
	

}
