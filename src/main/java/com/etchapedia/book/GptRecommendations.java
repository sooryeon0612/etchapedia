package com.etchapedia.book;

import java.time.LocalDate;

import com.etchapedia.user.Users;

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
public class GptRecommendations {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GptRecommendations")
	@SequenceGenerator(name="GptRecommendations", sequenceName="seq_recommend_idx", allocationSize=1)
	private Integer recommendIdx;
	
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	private LocalDate updateDate;

	
}
