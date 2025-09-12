package com.etchapedia.home;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Integer>{
	Optional<Book> findByIsbn(String isbn);
}
