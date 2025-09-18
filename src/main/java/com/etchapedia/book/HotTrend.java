package com.etchapedia.book;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class HotTrend {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HotTrend")
	@SequenceGenerator(name="HotTrend", sequenceName="seq_trend_idx", allocationSize=1)
	private Integer trendIdx;
	
	private LocalDate updateDate;

}
