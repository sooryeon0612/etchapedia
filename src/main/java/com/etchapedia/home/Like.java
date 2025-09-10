package com.etchapedia.home;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Like {
	// FK
	private Integer userIdx;
	
	// FK
	private Integer commentIdx;
	
	// FK
	private Integer replyIdx;
}
