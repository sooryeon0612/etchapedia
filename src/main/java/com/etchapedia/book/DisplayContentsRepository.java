package com.etchapedia.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayContentsRepository extends JpaRepository <DisplayContents, Integer> {
	List<DisplayContents> findAllByGpt_RecommendIdx(Integer recommendIdx);
}
