package com.etchapedia.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.etchapedia.api.BookApiUtil;
import com.etchapedia.book.DisplayContents;
import com.etchapedia.book.DisplayContentsRepository;
import com.etchapedia.book.GptRecommendations;
import com.etchapedia.book.GptRecommendationsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class BookService {
    private final PasswordEncoder pwEncoder;
    
	@Autowired
	private BookApiUtil util;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private GptRecommendationsRepository gRepo;
	@Autowired
	private DisplayContentsRepository dRepo;

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
	
	public List<Book> getRecommendedBooks(Integer userIdx) {
		List<Book> retList = new ArrayList<>();
		List<GptRecommendations> recommendedList = gRepo.findAllByUser_UserIdxOrderByRecommendIdx(userIdx);
		for(GptRecommendations g : recommendedList) {
			for(DisplayContents d : dRepo.findAllByGpt_RecommendIdx(g.getRecommendIdx())) {
				retList.add(d.getBook());
				if(retList.size() == 10) return retList;
			}
		}
		return retList;
	}
	
	

}
