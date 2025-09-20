package com.etchapedia.home;

import com.etchapedia.book.Book;
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
public class Hate {
	@ManyToOne
	@JoinColumn(name="book_idx")
	private Book book;
	
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="hate")
	@SequenceGenerator(name="hate", sequenceName="seq_hate_idx", allocationSize=1)
	private Integer hateIdx;
}
