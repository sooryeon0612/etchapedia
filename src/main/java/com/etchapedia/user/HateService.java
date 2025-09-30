package com.etchapedia.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;

@Service
public class HateService {
	@Autowired
	private HateRepository hRepo;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private UsersRepository uRepo;
	
	// 관심없어요 책 목록 조회
	public List<Book> getHateBooks(Integer userIdx) {
		List<Book> retList = new ArrayList<>();
		List<Hate> list = hRepo.findAllByUser_UserIdxOrderByHateIdxDesc(userIdx);
		for(Hate h : list) {
			retList.add(h.getBook());
		}
		return retList;
	}
    
    // 관심없어요 추가
    public void addDisinterest(Integer userIdx, Integer bookIdx) {
        Users user = uRepo.findById(userIdx)
                          .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bRepo.findById(bookIdx)
                         .orElseThrow(() -> new RuntimeException("Book not found"));

        // 이미 있는지 체크해서 중복 방지
        if (hRepo.findByUserAndBook(user, book).isPresent()) {
            return;
        }

        Hate hate = new Hate();
        hate.setUser(user);
        hate.setBook(book);
        hRepo.save(hate);
    }

    // 관심없어요 해제
    public void removeDisinterest(Integer userIdx, Integer bookIdx) {
        Users user = uRepo.findById(userIdx)
                          .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bRepo.findById(bookIdx)
                         .orElseThrow(() -> new RuntimeException("Book not found"));

        hRepo.findByUserAndBook(user, book)
             .ifPresent(h -> hRepo.delete(h));
    }
}
