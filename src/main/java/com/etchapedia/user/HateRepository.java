package com.etchapedia.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etchapedia.book.Book;

public interface HateRepository extends JpaRepository<Hate, Integer> {
	// '관심없어요' 한 책들 최신순 조회
	List<Hate> findAllByUser_UserIdxOrderByHateIdxDesc(Integer userIdx);

    // '관심없어요' 중복 방지 조회
    Optional<Hate> findByUserAndBook(Users user, Book book);
}
