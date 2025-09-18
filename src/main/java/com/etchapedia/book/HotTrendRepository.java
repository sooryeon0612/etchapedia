package com.etchapedia.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HotTrendRepository extends JpaRepository <HotTrend, Integer> {
	@Query(value="SELECT * FROM (SELECT * FROM hot_trend ORDER BY trend_idx DESC) WHERE ROWNUM = 1",
			nativeQuery=true)
	HotTrend findTop1ByOrderByTrendIdxDesc();
}
