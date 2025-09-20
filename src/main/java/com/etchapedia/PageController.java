package com.etchapedia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookService;
import com.etchapedia.book.HotTrendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	@Autowired
	private BookService bSvc;
	@Autowired
	private HotTrendService hSvc;

    // 홈 화면 (home.html)
    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws JsonMappingException, JsonProcessingException {
    	LocalDate today = LocalDate.now();
    	LocalDate lastTrend = hSvc.getLastUpdateDate();
    	if(!lastTrend.equals(today)) {
    		List<Book> trendList = hSvc.loadHotTrendBooks(today.minusDays(1).toString());
    		hSvc.logHotTrend(trendList);
    	}
    	model.addAttribute("popular", bSvc.getPopularBooks());
    	model.addAttribute("trend", hSvc.getHotTrendBooks());
        return "home";
    }
    
    // 책 검색 화면 (book_search.html)
    @GetMapping("/book/search")
    public String bookSearch(@RequestParam("query")String query, Model model) {
    	model.addAttribute("list", bSvc.getSearchBooks(query));
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