package com.etchapedia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.etchapedia.book.GptRecommendationsService;
import com.etchapedia.book.HotTrendService;
import com.etchapedia.home.Book;
import com.etchapedia.home.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	@Autowired
	private BookService bSvc;
	@Autowired
	private GptRecommendationsService gSvc;
	@Autowired
	private HotTrendService hSvc;

    // 홈 화면 (home.html)
    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws JsonMappingException, JsonProcessingException {
    	LocalDate today = LocalDate.now();
    	LocalDate yesterday = today.minusDays(1);
    	LocalDate lastUpdate = gSvc.getLastUpdateDate(1);
    	if(lastUpdate != today) {
    		List<Book> recommedList = gSvc.getGptRecommendBooks(1);
    		gSvc.logRecommendation(recommedList, 1);
    	}
    	model.addAttribute("popular", bSvc.getPopularBooks());
    	model.addAttribute("hotTrend", hSvc.getHotTrendBookList(yesterday.toString()));
    	model.addAttribute("ai", bSvc.getRecommendedBooks(1));
        return "home";
    }
    
    // 책 검색 화면 (book_search.html)
    @GetMapping("/book/search")
    public String bookSearch() {
    	return "book_search";
    }
    
    // 책 상세 화면 (detail_page.html)
    @GetMapping("/detail/page")
    public String detailPage() {
    	return "detail_page";
    }
    
    // 책 상세 화면 - 코멘트 (reply.html)
    @GetMapping("/reply")
    public String reply() {
    	return "reply";
    }
    
    // 소식 화면 (news.html)
    @GetMapping("/news")
    public String news() {
    	return "news";
    }
    
    // 마이페이지 (mypage.html)
    @GetMapping("/mypage")
    public String mypage() {
    	return "mypage";
    }
    
    // 장바구니 (cart.html)
    @GetMapping("/cart")
    public String cart() {
    	return "cart";
    }
    
    // test 장바구니 (cart2.html) css 수정중
    @GetMapping("/cart2")
    public String cart2() {
    	return "cart2";
    }
   
}