package com.etchapedia.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.api.BookApiUtil;
import com.etchapedia.home.Click;
import com.etchapedia.home.ClickRepository;
import com.etchapedia.user.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class GptRecommendationsService {
	@Autowired
	private GptRecommendationsRepository gRepo;
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private DisplayContentsRepository dRepo;
	@Autowired
	private ClickRepository cRepo;
	@Autowired
	private BookApiUtil util;
	@Autowired
	private BookRepository bRepo;
	
	// 마지막 업데이트 날짜 
	public LocalDate getLastUpdateDate(Integer userIdx) {
		return gRepo.findTop1ByUser_UserIdxOrderByRecommendIdxDesc(userIdx).getUpdateDate();
	}
	
	// 추천 받았던 내용 데베에 저장
	public void logRecommendation(List<Book> savedList, Integer userIdx) {
		GptRecommendations g = new GptRecommendations();
		g.setUser(uRepo.findById(userIdx).get());
		g.setUpdateDate(LocalDate.now());
		GptRecommendations logged = gRepo.save(g);
		
		for(Book b : savedList) {
			DisplayContents d = new DisplayContents();
			d.setBook(b);
			d.setGpt(logged);
			dRepo.save(d);
		}
	}
	
	// 클릭했던 책 리스트 
	public List<Book> getClikedBooks(Integer userIdx) {
		List<Book> clickList = new ArrayList<>();
		for(Click c : cRepo.findAllByUser_UserIdx(userIdx)){
			clickList.add(c.getBook());
		}
		return clickList;
	}
	
	// 지피티 추천 책 가져오고 없으면 데베에 저장 
	public List<Book> getGptRecommendBooks(Integer userIdx) throws JsonMappingException, JsonProcessingException {
		List<Book> clickList = getClikedBooks(userIdx);
		List<Book> gptList = new ArrayList<>();
		List<Book> retList = new ArrayList<>();

		gptList = util.getGptRecommendBooks(clickList);
		for(Book b : gptList) {
			b = util.findBookByTitleFromNaver(b);
			if(b.getIsbn().equals("-1")) continue;
			
			Optional<Book> ob = bRepo.findByIsbn(b.getIsbn());
			if(ob.isEmpty()) {
				b = util.getLoanByIsbn(b);
				bRepo.save(b);
				retList.add(b);
			} else {
				retList.add(ob.get());
			}
		}
		return retList;
	}
	
	// 가장 최근에 저장됐던 gpt추천 책 10권 
	public List<Book> getRecommendedBooks(Integer userIdx) {
		List<Book> retList = new ArrayList<>();
		Set<Integer> idxList = new HashSet<>();
		List<GptRecommendations> recommendedList = gRepo.findAllByUser_UserIdxOrderByRecommendIdx(userIdx);
		for(GptRecommendations g : recommendedList) {
			for(DisplayContents d : dRepo.findAllByGpt_RecommendIdx(g.getRecommendIdx())) {
				idxList.add(d.getBook().getBookIdx());
				if(idxList.size() == 10) break;
			}
			if(idxList.size() == 10) break;
		}
		for(Integer idx : idxList) {
			retList.add(bRepo.findById(idx).get());
		}
		return retList;
	}

}
