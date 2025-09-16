package com.etchapedia.home;

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
    
    private static String[] bad = {":", "=", "/"};
	
	public void saveBooks(int pageNo, int pageSize) throws JsonMappingException, JsonProcessingException {
		List<Book> bookList = util.loadBookFromApi(pageNo, pageSize);
		int save = 0;
		
		for(Book b : bookList) {
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
	
	public List<Book> getHotTrendBookList(String searchDt) throws JsonMappingException, JsonProcessingException {
		List<Book> rawList = util.loadHotTrendBookList(searchDt);
		List<Book> retList = new ArrayList<>();
		
		for(Book b : rawList) {
			boolean exist = false;
			for(Book saved : retList) {
				if(b.getIsbn().equals(saved.getIsbn())) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				Optional<Book> om = bRepo.findByIsbn(b.getIsbn());
				
				String title = b.getTitle();
				for(int i = 0; i < bad.length; i++) {
					int cut = title.indexOf(bad[i]);
					if(cut != -1) {
						title = title.substring(0, title.indexOf(bad[i]));
						b.setTitle(title);
					}
				}
				if(om.isEmpty()) bRepo.save(b);
				retList.add(b);
			}
		}
		
		return retList;
 	}
	
	public List<Book> getPopularBooks() {
		return bRepo.findTop10ByOrderByLoanDesc();
	}
	
	

}
