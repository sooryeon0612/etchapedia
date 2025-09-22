package com.etchapedia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsRepository;

@Controller
public class BookController {
    @Autowired
    private BookRepository bRepo;
    @Autowired
    private CommentsRepository cmRepo;
    
    // 책 상세 화면 - 코멘트
    @GetMapping("/detail/page")
    public String showBookDetail(@RequestParam("id") Integer bookIdx, Model model) {
    	Book book = bRepo.findById(bookIdx).orElse(null);
        model.addAttribute("book", book);

        List<Comments> comments;
        comments = cmRepo.findByBook_BookIdx(bookIdx);
        model.addAttribute("comments", comments);

        return "detail_page";
    }
}