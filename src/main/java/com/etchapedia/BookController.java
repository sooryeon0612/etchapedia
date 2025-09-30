package com.etchapedia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.book.ClickService;
import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsRepository;
import com.etchapedia.security.CustomUserDetails;

@Controller
public class BookController {
    @Autowired
    private BookRepository bRepo;
    @Autowired
    private CommentsRepository cmRepo;
    @Autowired
    private ClickService cSvc;
    
    // 책 상세 화면 - 코멘트
    @GetMapping("/detail/page")
    public String showBookDetail(@RequestParam("id") Integer bookIdx, Model model,
    							 @AuthenticationPrincipal CustomUserDetails userDetails) {
    	Book book = bRepo.findById(bookIdx).orElse(null);
        model.addAttribute("book", book);
       
        List<Comments> comments;
        comments = cmRepo.findByBook_BookIdx(bookIdx);
        model.addAttribute("comments", comments);
        
        if(userDetails != null)
        	cSvc.saveClickedBook(bookIdx, userDetails.getUserIdx());
        return "detail_page";
    }
}