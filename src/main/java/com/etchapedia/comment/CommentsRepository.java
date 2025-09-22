package com.etchapedia.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	List<Comments> findByBook_BookIdx(Integer bookIdx);

}
