package com.etchapedia.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click, Integer>{
	List<Click> findAllByUser_UserIdx(Integer userIdx);

}
