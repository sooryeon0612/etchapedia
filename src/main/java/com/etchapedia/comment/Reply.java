package com.etchapedia.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reply {
	@Id
	private Integer replyIdx;
	
	// FK
	private Integer userIdx;
	
	// FK
	private Integer commentIdx;
	
	private String reply;
	
}
