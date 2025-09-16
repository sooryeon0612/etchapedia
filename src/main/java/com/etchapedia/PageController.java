package com.etchapedia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 홈 화면 (home.html)
    @GetMapping("/home")
    public String home() {
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
    
    // test 장바구니
    @GetMapping("/cart2")
    public String cart2() {
    	return "cart2";
    }
   
}