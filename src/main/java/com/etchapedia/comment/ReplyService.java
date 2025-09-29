package com.etchapedia.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.user.UsersRepository;

@Service
public class ReplyService {
	@Autowired
	private ReplyRepository rRepo;
	@Autowired
	private CommentsRepository cRepo;
	@Autowired
	private UsersRepository uRepo;
	
	public List<Reply> getRepliesByCommentIdx(Integer commentIdx) {
		return rRepo.findAllByComment_CommentIdxOrderByReplyIdx(commentIdx);
	}
	
	public void insertReply(Integer userIdx, Integer commentIdx, String content) {
		Reply r = new Reply();
		r.setComment(cRepo.findById(commentIdx).get());
		r.setUser(uRepo.findById(commentIdx).get());
		r.setReply(content);
		rRepo.save(r);
	}

}
