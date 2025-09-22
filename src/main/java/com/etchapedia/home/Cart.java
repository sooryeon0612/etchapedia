package com.etchapedia.home;

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
public class Cart {
	@ManyToOne
	@JoinColumn(name="book_idx")
	private Book book;
	
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	private Integer quantity;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cart")
	@SequenceGenerator(name="cart", sequenceName="seq_cart_idx", allocationSize=1)
	private Integer cartIdx;
}
