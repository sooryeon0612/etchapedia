package com.etchapedia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookService;
import com.etchapedia.book.HotTrendService;
import com.etchapedia.comment.CommentsService;
import com.etchapedia.comment.ReplyService;
import com.etchapedia.security.CustomUserDetails;
import com.etchapedia.user.HateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class PageController {
	@Autowired
	private BookService bSvc;
	@Autowired
	private HotTrendService tSvc;
	@Autowired
	private HateService hSvc;
	@Autowired
	private ReplyService rSvc;
	@Autowired
	private CommentsService cSvc;

    // 홈 화면 (home.html)
    @GetMapping("/home")
    public String home(Model model) throws JsonMappingException, JsonProcessingException {
    	LocalDate today = LocalDate.now();
    	LocalDate lastTrend = tSvc.getLastUpdateDate();
    	if(!lastTrend.equals(today)) {
    		List<Book> trendList = tSvc.loadHotTrendBooks(today.minusDays(1).toString());
    		tSvc.logHotTrend(trendList);
    	}
    	model.addAttribute("popular", bSvc.getPopularBooks());
    	model.addAttribute("trend", tSvc.getHotTrendBooks());
        return "home";
    }
    
    // 책 검색 화면 (book_search.html)
    @GetMapping("/book/search")
    public String bookSearch(@RequestParam("query")String query, Model model) throws JsonMappingException, JsonProcessingException {
    	model.addAttribute("list", bSvc.getSearchBooks(query));
    	model.addAttribute("dictionary", bSvc.searchDictionary(query));
    	return "book_search";
    }
    
    // 책 상세 화면 - 댓글 (reply.html)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reply")
    public String reply(Model model, @RequestParam("commentIdx")Integer commentIdx,
    					@AuthenticationPrincipal CustomUserDetails userDetails) {
    	model.addAttribute("info", cSvc.getCommentsInfo(commentIdx, userDetails.getUserIdx()));
    	model.addAttribute("replies", rSvc.getRepliesByCommentIdx(commentIdx));
    	return "reply";
    }
    
    // 코멘트에 댓글 달기
    @PostMapping("/reply/add")
    public String insertReply(@RequestParam("commentIdx")Integer commentIdx,
    						  @RequestParam("userIdx")Integer userIdx,
    						  @RequestParam("content")String content) {
    	rSvc.insertReply(userIdx, commentIdx, content);
    	return "redirect:/reply?commentIdx=" + commentIdx;
    }
    
    
    // 소식 화면 (news.html)
    @GetMapping("/news")
    public String news() {
    	return "news";
    }
    
    // 마이페이지 (mypage.html)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails,
    					 Model model) {
    	model.addAttribute("hate", hSvc.getHateBooks(userDetails.getUserIdx()));
    	return "mypage";
    }
}