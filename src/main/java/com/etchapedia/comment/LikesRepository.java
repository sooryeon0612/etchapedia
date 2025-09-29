package com.etchapedia.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
	List<Likes> findAllByComment_CommentIdx(Integer commentIdx);
	List<Likes> findAllByReply_ReplyIdx(Integer replyIdx);
	void deleteByUser_UserIdxAndComment_CommentIdx(Integer userIdx, Integer commentIdx);
	void deleteByUser_UserIdxAndReply_ReplyIdx(Integer userIdx, Integer replyIdx);
}
