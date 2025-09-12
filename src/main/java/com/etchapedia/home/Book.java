package com.etchapedia.home;

import jakarta.persistence.Column;
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
public class Book {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book")
	@SequenceGenerator(name="book", sequenceName="seq_book_idx", allocationSize=1)
	private Integer bookIdx;
	
	private String title;
	private String author;
	
	@Column(length=4000)
	private String description;
	
	@Column(length=2000)
	private String pic;
	
	private Double rate;
	private Integer viewCount;
	private Integer price;
	private String isbn;
}
