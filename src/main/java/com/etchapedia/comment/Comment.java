package com.etchapedia.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
	@Id
	private Integer commentIdx;
	
	// FK
	private Integer userIdx;
	
	private String comment;
}
