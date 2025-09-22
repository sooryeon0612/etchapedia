package com.etchapedia.comment;

import com.etchapedia.home.Book;
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
public class Comments {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comments")
	@SequenceGenerator(name="comments", sequenceName="seq_comment_idx", allocationSize=1)
	private Integer commentIdx;
	
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	private String content;
	
	@ManyToOne
	@JoinColumn(name="book_idx")
	private Book book;
	
}
