package com.etchapedia.book;

import com.etchapedia.home.Book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DisplayContents {
	@ManyToOne
	@JoinColumn(name="recommend_idx")
	private GptRecommendations gpt;
	
	@ManyToOne
	@JoinColumn(name="trend_idx")
	private HotTrend trend;
	
	@ManyToOne
	@JoinColumn(name="book_idx")
	private Book book;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DisplayContents")
	@SequenceGenerator(name="DisplayContents", sequenceName="seq_content_idx", allocationSize=1)
	private Integer contentIdx;
}
