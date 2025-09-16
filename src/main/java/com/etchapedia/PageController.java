package com.etchapedia;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.etchapedia.home.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class PageController {
	@Autowired
	private BookService bSvc;

    // 홈 화면 (home.html)
    @GetMapping("/home")
    public String home(Model model) throws JsonMappingException, JsonProcessingException {
    	LocalDate yesterday = LocalDate.now().minusDays(1);
    	model.addAttribute("popular", bSvc.getPopularBooks());
    	model.addAttribute("hotTrend", bSvc.getHotTrendBookList(yesterday.toString()));
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