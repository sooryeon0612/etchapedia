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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class HotTrendService {
	@Autowired
	private BookApiUtil util;
	
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private HotTrendRepository hRepo;
	@Autowired
	private DisplayContentsRepository dRepo;
	
	private static String[] bad = {":", "=", "/", "(", ")"};
	
	// 인기 급상승 도서 기록 저장 
	public void logHotTrend(List<Book> savedList) {
		HotTrend h = new HotTrend();
		h.setUpdateDate(LocalDate.now());
		HotTrend logged = hRepo.save(h);
		
		for(Book b : savedList) {
			DisplayContents d = new DisplayContents();
			d.setBook(b);
			d.setTrend(logged);
			dRepo.save(d);
		}
	}
	
	// 인기 급상승 도서 가져오고 없으면 데베에 저장
	public List<Book> loadHotTrendBooks(String searchDt) throws JsonMappingException, JsonProcessingException {
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
				if(om.isPresent()) {
					retList.add(om.get());
					continue;
				}
				
				b = util.getBookInfoFromNaver(b);
				if(b.getIsbn().equals("-1")) continue;
				b = util.getLoanByIsbn(b);
				
				String title = b.getTitle();
				for(int i = 0; i < bad.length; i++) {
					int cut = title.indexOf(bad[i]);
					if(cut != -1) {
						title = title.substring(0, title.indexOf(bad[i]));
						b.setTitle(title);
					}
				}
				if(title.equals("")) continue;
				bRepo.save(b);
				retList.add(b);
			}
		}
		return retList;
 	}
	
	// 가장 최근에 저장된 인기 급상승 10권 
	public List<Book> getHotTrendBooks() {
		List<Book> retList = new ArrayList<>();
		Set<Integer> idxList = new HashSet<>();
		for(DisplayContents d : dRepo.findTop10ByOrderByTrend_TrendIdxDesc()) {
			idxList.add(d.getBook().getBookIdx());
			if(idxList.size() == 10) break;
		}
		for(Integer idx : idxList) {
			retList.add(bRepo.findById(idx).get());
		}
		return retList;
	}
	
	// 마지막 업데이트 날짜 
	public LocalDate getLastUpdateDate() {
		return hRepo.findTop1ByOrderByTrendIdxDesc().getUpdateDate();
	}
}
