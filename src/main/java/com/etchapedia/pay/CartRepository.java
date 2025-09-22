package com.etchapedia.pay;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etchapedia.book.Book;
import com.etchapedia.user.Users;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	int countByUser(Users user);
	
	List<Cart> findByUser(Users user);

	Optional<Cart> findByUserAndBook(Users user, Book book);
}