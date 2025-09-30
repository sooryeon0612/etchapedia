package com.etchapedia;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookRepository;
import com.etchapedia.book.ClickService;
import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsRepository;
import com.etchapedia.comment.CommentsService;
import com.etchapedia.pay.CartService;
import com.etchapedia.user.HateService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;
import com.etchapedia.security.CustomUserDetails;

@Controller
public class BookController {
    @Autowired
    private BookRepository bRepo;
    @Autowired
    private CommentsRepository cmRepo;
    @Autowired
    private CommentsService cSvc;
    @Autowired
    private UsersRepository uRepo;
    @Autowired
    private CartService csSvc;
    @Autowired
    private HateService hSvc;
	@Autowired
    private ClickService clSvc;

	// 관심없어요 추가/삭제
    @PostMapping("/book/disinterest")
    @ResponseBody
    public Map<String, Object> hateBook(@RequestBody Map<String, Object> payload,
                                                 @AuthenticationPrincipal UserDetails user) {
        Integer bookIdx = Integer.valueOf(payload.get("bookIdx").toString());
        boolean hateIdx = Boolean.parseBoolean(payload.get("hateIdx").toString());

        Users loginUser = uRepo.findByEmail(user.getUsername())
                               .orElseThrow(() -> new RuntimeException("User not found"));

        boolean newState;
        if (hateIdx) {
            hSvc.addDisinterest(loginUser.getUserIdx(), bookIdx);
            newState = true;
        } else {
            hSvc.removeDisinterest(loginUser.getUserIdx(), bookIdx);
            newState = false;
        }

        return Map.of("bookIdx", bookIdx, "hateIdx", hateIdx);
    }
    
	// 책 상세 화면 - 코멘트
    @GetMapping("/detail/page")
    public String showBookDetail(@RequestParam("id") Integer bookIdx, Model model,
                                 Principal principal,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Book book = bRepo.findById(bookIdx).orElse(null);
        model.addAttribute("book", book);

        List<Comments> comments = cmRepo.findByBook_BookIdx(bookIdx);
        model.addAttribute("comments", comments);

        // 로그인한 사용자 장바구니 개수 조회
        int cartCount = 0;
        if (principal != null) {
            Users user = uRepo.findByEmail(principal.getName()) 
                              .orElse(null);
            if (user != null) {
                cartCount = csSvc.getCartItemCount(user);
            }
        }
        model.addAttribute("cartCount", cartCount);

        if(userDetails != null)
        	clSvc.saveClickedBook(bookIdx, userDetails.getUserIdx());

        return "detail_page";
    }

    
    // 책 상세 화면 - 코멘트 저장
    @PostMapping("/comments")
    @ResponseBody
    public Map<String, Object> saveComment(@RequestBody Comments comments,
                                           @AuthenticationPrincipal UserDetails user) {
        Map<String, Object> result = new HashMap<>();
        try {
        	Users loginUser = uRepo.findByEmail(user.getUsername())
                                      .orElseThrow(() -> new RuntimeException("User not found"));

            // 작성자 세팅
            comments.setUser(loginUser);

            // 댓글 저장
            Comments saved = cSvc.save(comments);

            result.put("success", true);
            result.put("comment", saved);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}