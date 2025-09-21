package com.etchapedia.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.book.Book;

@Service
public class HateService {
	@Autowired
	private HateRepository hRepo;
	
	public List<Book> getHateBooks(Integer userIdx) {
		List<Book> retList = new ArrayList<>();
		List<Hate> list = hRepo.findAllByUser_UserIdxOrderByHateIdxDesc(userIdx);
		for(Hate h : list) {
			retList.add(h.getBook());
		}
		return retList;
	}
}
