package com.etchapedia.home;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {
	@Id
	private Integer bookIdx;
	
	private String title;
	private String writer;
	private String description;
	private String pic;
	private Integer rate;
	private Integer viewCount;
	private Integer price;
}
