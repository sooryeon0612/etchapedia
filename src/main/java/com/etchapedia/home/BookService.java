package com.etchapedia.home;

import java.util.List;

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
    
    private static String[] bad = {":", "=", "/", "(", ")"};
	
	public void saveBooks(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		List<Book> bookList = util.loadBooksFromLibrary(pageNo, pageSize);
		int save = 0;
		
		for(Book b : bookList) {
			b = util.getBookInfoFromNaver(b);
			if(b.getIsbn().equals("-1")) continue;
			
			String title = b.getTitle();
			for(int i=0; i<bad.length; i++) {
				int cut = title.indexOf(bad[i]);
				if(cut != -1) {
					title = title.substring(0, title.indexOf(bad[i]));
					b.setTitle(title);
				}
			}
			bRepo.save(b);
			save++;
		}
		System.out.println("saved : " + save);
	}
	
	public List<Book> getPopularBooks() {
		return bRepo.findTop10ByOrderByLoanDesc();
	}
	
	
	
	

}
