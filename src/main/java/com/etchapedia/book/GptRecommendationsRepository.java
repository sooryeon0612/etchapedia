package com.etchapedia.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GptRecommendationsRepository extends JpaRepository <GptRecommendations, Integer> {
	List<GptRecommendations> findAllByUser_UserIdxOrderByRecommendIdx(Integer userIdx);
	
	@Query(value="SELECT * FROM (SELECT * FROM gpt_recommendations WHERE user_idx = :userIdx ORDER BY recommend_idx DESC) WHERE ROWNUM = 1",
			nativeQuery=true)
	GptRecommendations findTop1ByUser_UserIdxOrderByRecommendIdxDesc(@Param("userIdx") Integer userIdx);
	
}
