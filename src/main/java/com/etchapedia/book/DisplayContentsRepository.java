package com.etchapedia.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DisplayContentsRepository extends JpaRepository <DisplayContents, Integer> {
	List<DisplayContents> findAllByGpt_RecommendIdx(Integer recommendIdx);
	
	@Query(value="SELECT * FROM (SELECT * FROM display_contents ORDER BY trend_idx DESC) WHERE ROWNUM <= 10",
			nativeQuery=true)
	List<DisplayContents> findTop10ByOrderByTrend_TrendIdxDesc();
}
