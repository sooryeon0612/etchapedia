package com.etchapedia.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GptRecommendationsRepository extends JpaRepository <GptRecommendations, Integer> {
	List<GptRecommendations> findAllByUser_UserIdxOrderByRecommendIdx(Integer userIdx);
	GptRecommendations findTop1ByUser_UserIdxOrderByRecommendIdxDesc(Integer userIdx);
	
}
