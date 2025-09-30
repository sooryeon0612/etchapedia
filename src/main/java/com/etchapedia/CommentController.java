package com.etchapedia;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etchapedia.comment.Comments;
import com.etchapedia.comment.CommentsService;
import com.etchapedia.comment.ReplyService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;

@Controller
public class CommentController {
	@Autowired
	private ReplyService rSvc;
	@Autowired
    private CommentsService cSvc;
	@Autowired
    private UsersRepository uRepo;
	
	// 작업자 : 서수련
    // 기능 : 코멘트에 댓글 달기
    @PostMapping("/reply/add")
    public String insertReply(@RequestParam("commentIdx")Integer commentIdx,
    						  @RequestParam("userIdx")Integer userIdx,
    						  @RequestParam("content")String content) {
    	rSvc.insertReply(userIdx, commentIdx, content);
    	return "redirect:/reply?commentIdx=" + commentIdx;
    }
    
    // 작업자 : 이경미
    // 기능 : 특정 책에 코멘트 저장
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
