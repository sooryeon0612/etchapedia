package com.etchapedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.etchapedia.comment.ReplyService;

@Controller
public class CommentController {
	@Autowired
	private ReplyService rSvc;
	
	// 작업자 : 서수련
    // 기능 : 코멘트에 댓글 달기
    @PostMapping("/reply/add")
    public String insertReply(@RequestParam("commentIdx")Integer commentIdx,
    						  @RequestParam("userIdx")Integer userIdx,
    						  @RequestParam("content")String content) {
    	rSvc.insertReply(userIdx, commentIdx, content);
    	return "redirect:/reply?commentIdx=" + commentIdx;
    }

}
