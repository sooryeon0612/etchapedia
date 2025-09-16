package com.etchapedia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.etchapedia.api.BookApiUtil;
import com.etchapedia.home.Book;
import com.etchapedia.home.BookService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest
class EtchapediaApplicationTests {
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private PasswordEncoder pwEncoder;
	@Autowired
	private BookService bSvc;
	@Autowired
	private BookApiUtil util;
	

	@Test
	void testInsertDummy() {
		Users users = new Users();
		users.setEmail("1234@1234");
		users.setName("test");
		users.setPassword(pwEncoder.encode("1234"));
		users.setProfile(null);
		uRepo.save(users);
	}


	@Test
	void getUserByEmail() {
		Users users = new Users();
		users.setEmail("1234@1234");
		users.setName("test");
		users.setPassword(pwEncoder.encode("1234"));
		users.setProfile(null);
		uRepo.save(users);
		
		Optional<Users> ou = uRepo.findByEmail("1234@1234");
		assertTrue(ou.isPresent());
		Users u = ou.get();
		System.out.println(u.getEmail() + " / " + u.getPassword());
	}
	
	@Test
	void testSaveBooks() throws JsonMappingException, JsonProcessingException {
		bSvc.saveBooks(6, 10);
//		aSvc.saveBooks(2, 10);
//		aSvc.saveBooks(3, 10);
//		aSvc.saveBooks(4, 10);
//		aSvc.saveBooks(5, 10);
	}
	
	@Test
	void testHotTrendBookList() throws JsonMappingException, JsonProcessingException {
		List<Book> list = bSvc.getHotTrendBookList("2025-09-15");
		for(Book b : list) System.out.println(b.getTitle());
	}
	
	@Test
	void testGetLoanByIsbn() throws JsonMappingException, JsonProcessingException {
		Book b = new Book();
		b.setIsbn("9788954646079");
		util.getLoanByIsbn(b);
		System.out.println(b.getLoan());
	}


}
