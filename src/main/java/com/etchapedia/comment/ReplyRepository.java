package com.etchapedia.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	List<Reply> findAllByComment_CommentIdxOrderByReplyIdx(Integer commentIdx);
}
