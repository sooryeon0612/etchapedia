package com.etchapedia.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.user.UsersRepository;

@Service
public class ClickService {
	@Autowired
	private ClickRepository cRepo;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private UsersRepository uRepo;
	
	public void saveClickedBook(Integer bookIdx, Integer userIdx) {
		Click c = new Click();
		c.setBook(bRepo.findById(bookIdx).get());
		c.setUser(uRepo.findById(userIdx).get());
		cRepo.save(c);
	}
}
