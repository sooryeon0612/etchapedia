package com.etchapedia.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HateRepository extends JpaRepository<Hate, Integer> {
	List<Hate> findAllByUser_UserIdxOrderByHateIdxDesc(Integer userIdx);
}
