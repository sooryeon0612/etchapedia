package com.etchapedia.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etchapedia.user.UsersRepository;

@Service
public class LikesService {
	@Autowired
	private LikesRepository lRepo;
	@Autowired
	private ReplyRepository rRepo;
	@Autowired
	private CommentsRepository cRepo;
	@Autowired
	private UsersRepository uRepo;
	
	public Map<Integer, List<Likes>> getReplyLikesByCommentIdx(Integer commentIdx) {
		Map<Integer, List<Likes>> retMap = new HashMap<>();
		for(Reply r : rRepo.findAllByComment_CommentIdxOrderByReplyIdx(commentIdx)) {
			retMap.put(r.getReplyIdx(), lRepo.findAllByReply_ReplyIdx(r.getReplyIdx()));
		}
		return retMap;
	}
	
	public void insertCommentLike(Integer userIdx, Integer commentIdx) {
		Likes like = new Likes();
	    like.setComment(cRepo.findById(commentIdx).get());
	    like.setUser(uRepo.findById(userIdx).get());
	    lRepo.save(like);
	}
	
	@Transactional
	public void deleteCommentLike(Integer userIdx, Integer commentIdx) {
		lRepo.deleteByUser_UserIdxAndComment_CommentIdx(userIdx, commentIdx);
	}
	
	public void insertReplyLike(Integer userIdx, Integer replyIdx) {
		Likes like = new Likes();
	    like.setReply(rRepo.findById(replyIdx).get());
	    like.setUser(uRepo.findById(userIdx).get());
	    lRepo.save(like);
	}
	
	@Transactional
	public void deleteReplyLike(Integer userIdx, Integer replyIdx) {
		lRepo.deleteByUser_UserIdxAndReply_ReplyIdx(userIdx, replyIdx);
	}
	

}
