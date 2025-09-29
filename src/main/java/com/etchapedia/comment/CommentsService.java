package com.etchapedia.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
	@Autowired
	private CommentsRepository cRepo;
	@Autowired
	private LikesRepository lRepo;
	@Autowired
	private ReplyRepository rRepo;
	
	public CommentsInfoDto getCommentsInfo(Integer commentIdx, Integer userIdx) {
		CommentsInfoDto dto = new CommentsInfoDto();
		dto.setComment(cRepo.findById(commentIdx).get());
		dto.setLikesCount(lRepo.findAllByComment_CommentIdx(commentIdx).size());
		dto.setReplies(rRepo.findAllByComment_CommentIdxOrderByReplyIdx(commentIdx));
		dto.setLiked(checkCommnetLikeByUserIdx(commentIdx, userIdx));
		return dto;
	}
	
	public boolean checkCommnetLikeByUserIdx(Integer commentIdx, Integer userIdx) {
		List<Likes> list = lRepo.findAllByComment_CommentIdx(commentIdx);
		for(Likes l : list) {
			if(l.getUser().getUserIdx() == userIdx) return true;
		}
		return false;
	}
	
	
}
