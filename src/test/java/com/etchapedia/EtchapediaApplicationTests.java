package com.etchapedia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.etchapedia.api.BookApiUtil;
import com.etchapedia.api.NaverApiClient;
import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.book.BookService;
import com.etchapedia.book.GptRecommendationsService;
import com.etchapedia.book.HotTrendService;
import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsRepository;
import com.etchapedia.comment.Likes;
import com.etchapedia.comment.LikesRepository;
import com.etchapedia.comment.Reply;
import com.etchapedia.comment.ReplyRepository;
import com.etchapedia.comment.ReplyService;
import com.etchapedia.user.Click;
import com.etchapedia.user.ClickRepository;
import com.etchapedia.user.Hate;
import com.etchapedia.user.HateRepository;
import com.etchapedia.user.HateService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;
import com.etchapedia.user.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest
class EtchapediaApplicationTests {
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private ClickRepository cRepo;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private UsersService uSvc;
	@Autowired
	private HateRepository hRepo;
	
	@Autowired
	private PasswordEncoder pwEncoder;
	@Autowired
	private BookService bSvc;
	@Autowired
	private GptRecommendationsService gSvc;
	@Autowired
	private HotTrendService hSvc;
	@Autowired
	private BookApiUtil util;
	@Autowired
	private HateService haSvc;
	@Autowired
	private ReplyService rSvc;
	
	@Autowired
	private NaverApiClient naver;
	
	@Autowired
	private CommentsRepository cmRepo;
	@Autowired
	private LikesRepository lRepo;
	@Autowired
	private ReplyRepository rRepo;
	
	@Test
	void testInsertCommentDummy() {
	    // 1. 테스트용 사용자 생성 및 저장
	    Users user1 = new Users();
	    user1.setName("테스트");
	    user1.setEmail("test@naver.com");
	    // 사용자의 비밀번호도 설정해주는 것이 좋습니다.
	    // user1.setPassword(pwEncoder.encode("password"));
	    uRepo.save(user1);

	    // 2. 이미 존재하는 책 가져오기
	    // 데이터베이스에 bookIdx가 1과 2인 책이 있다고 가정합니다.
	    Book book1 = bRepo.findById(1)
	                      .orElseThrow(() -> new RuntimeException("책 ID 1을 찾을 수 없습니다."));

	    // 3. 코멘트 생성 및 저장
	    Comments comment1 = new Comments();
	    comment1.setUser(user1);
	    comment1.setBook(book1);
	    comment1.setContent("소리 지르면서 읽었어요.");
	    cmRepo.save(comment1);

	    // 4. 좋아요(Likes) 생성 및 저장
	    Likes like1 = new Likes();
	    like1.setComment(comment1);
	    like1.setUser(user1);
	    lRepo.save(like1);

	    // 5. 답글(Replies) 생성 및 저장
	    Reply reply1 = new Reply();
	    reply1.setComment(comment1);
	    reply1.setUser(user1);
	    reply1.setReply("한번 더 읽기 위한 저장!");
	    rRepo.save(reply1);

	    System.out.println("댓글, 좋아요, 답글 더미 데이터가 성공적으로 삽입되었습니다.");
	}
	
	@Test
	void testINSERTDUMMY() throws JsonMappingException, JsonProcessingException {
		// test 유저 저장 
//		Users users = new Users();
//		users.setEmail("test@1234");
//		users.setName("test");
//		users.setPassword(pwEncoder.encode("1234"));
//		users.setProfile(null);
//		uRepo.save(users);
////		
////		// 베이스 책들 저장 
//		bSvc.saveBooks(1, 10);
//		bSvc.saveBooks(2, 10);
//		bSvc.saveBooks(3, 10);
//		bSvc.saveBooks(4, 10);
//		bSvc.saveBooks(5, 10);
//		
//		// 클릭한 책 저장 
//		Click c = new Click();
//		c.setBook(bRepo.findById(1).get());
//		c.setUser(uRepo.findById(1).get());
//		cRepo.save(c);
//		Click c2 = new Click();
//		c2.setBook(bRepo.findById(2).get());
//		c2.setUser(uRepo.findById(1).get());
//		cRepo.save(c2);
//		
//		// 지피티 추천 받기
//		List<Book> recommedList = gSvc.getGptRecommendBooks(1);
//		gSvc.logRecommendation(recommedList, 1);
//		
//		// 인기 급상승 받기
//		List<Book> trendList = hSvc.loadHotTrendBooks(LocalDate.now().minusDays(1).toString());
//		hSvc.logHotTrend(trendList);
//		
		// 관심 없어요 저장
		Hate h = new Hate();
		h.setBook(bRepo.findById(3).get());
		h.setUser(uRepo.findById(1).get());
		hRepo.save(h);
		Hate h2 = new Hate();
		h2.setBook(bRepo.findById(4).get());
		h2.setUser(uRepo.findById(1).get());
		hRepo.save(h2);
	}

	@Test
	void testInsertUserDummy() {
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
		
		bSvc.saveBooks(1, 10);
		bSvc.saveBooks(2, 10);
		bSvc.saveBooks(3, 10);
		bSvc.saveBooks(4, 10);
		bSvc.saveBooks(5, 10);
//		bSvc.saveBooks(6, 10);
	}
	
	@Test
	void testInsertClickDummy() {
		Click c = new Click();
		c.setBook(bRepo.findById(85).get());
		c.setUser(uRepo.findById(1).get());
		cRepo.save(c);
		Click c2 = new Click();
		c2.setBook(bRepo.findById(79).get());
		c2.setUser(uRepo.findById(1).get());
		cRepo.save(c2);
	}
	
	@Test
	void testHotTrendBookList() throws JsonMappingException, JsonProcessingException {
//		List<Book> list = hSvc.getHotTrendBookList("2025-09-15");
//		for(Book b : list) System.out.println(b.getTitle());
	}
	
	@Test
	void testGetLoanByIsbn() throws JsonMappingException, JsonProcessingException {
		Book b = new Book();
		b.setIsbn("9788954646079");
		util.getLoanByIsbn(b);
		System.out.println(b.getLoan());
	}
	
	@Test
	void testGetPopularBooks() {
		for(Book b : bSvc.getPopularBooks()) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testGetToayDate() {
		System.out.println(LocalDate.now().minusDays(1));
	}
	
	@Test
	void testGetHotTrend() throws JsonMappingException, JsonProcessingException {
//		for(Book b : hSvc.loadHotTrendBooks(LocalDate.now().minusDays(1).toString())) {
//		for(Book b : util.loadHotTrendBookList(LocalDate.now().minusDays(1).toString())) {
		for(Book b : hSvc.getHotTrendBooks()) {
			System.out.println(b.getTitle() + " / " + b.getBookIdx());
		}
	}
	
	
	@Test
	void testCallGptApi() throws JsonMappingException, JsonProcessingException {
		List<Book> list = new ArrayList<>();
		Book b1 = new Book();
		b1.setTitle("구의 증명");
		b1.setAuthor("최진영");
		list.add(b1);
		Book b2 = new Book();
		b2.setTitle("급류");
		b2.setAuthor("정대건");
		list.add(b2);
 		for(Book b : util.getGptRecommendBooks(list)) {
 			System.out.println(b.getTitle());
 		}
	}
	
	@Test
	void testNaverApi() throws JsonMappingException, JsonProcessingException {
//		String result = naver.callNaverApiByKeyword("최진영");
//		System.out.println(result);
		
		for(Book b : util.findBooksByKeywordFromNaver("최진영")) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testFindAllByUserIdx() {
		Users u = new Users();
		Optional<Users> ou = uRepo.findById(1);
//		for(Click c : cRepo.findAllByUser(ou.get())) {
//			System.out.println(c.getBook().getAuthor());
//		}
	}
	
	@Test
	void testGptBookFinal() throws JsonMappingException, JsonProcessingException {
		for(Book b : gSvc.getRecommendedBooks(1)) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testHotBooksFinal() throws JsonMappingException, JsonProcessingException {
		for(Book b : hSvc.getHotTrendBooks()) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testLocalDateSyso() {
		LocalDate today = LocalDate.now();
    	LocalDate lastTrend = hSvc.getLastUpdateDate();
    	LocalDate lastUpdate = gSvc.getLastUpdateDate(1);
    	System.out.println(today);
    	System.out.println(lastTrend);
    	System.out.println(lastUpdate);
    	System.out.println(today.equals(lastUpdate));
	}
	
	@Test
	void testbRepoMethod() {
		for(Book b : bRepo.findByAuthorContaining("게이고")) {
			System.out.println(b.getTitle() + " / " + b.getAuthor());
		}
	}
	
	@Test
	void testGetSearchBooks() {
		for(Book b : bSvc.getSearchBooks("최진영")) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testWikiApi() throws JsonMappingException, JsonProcessingException {
		System.out.println(util.getAnswerFromWiki("최진영"));
	}
	
	@Test
	void testCheckUserByPw() {
		System.out.println(uSvc.checkUserByPw(1, "1234"));
	}
	
	@Test
	void testHateList() {
		for(Book b : haSvc.getHateBooks(1)) {
			System.out.println(b.getTitle());
		}
	}
	
	@Test
	void testInsertReplyDummy() {
//		Reply r1 = new Reply();
//		r1.setComment(cmRepo.findById(1).get());
//		r1.setReply("감 먹을래?");
//		r1.setUser(uRepo.findById(1).get());
//		rRepo.save(r1);
		Likes like1 = new Likes();
	    like1.setReply(rRepo.findById(2).get());
	    like1.setUser(uRepo.findById(1).get());
	    lRepo.save(like1);
	}

}
