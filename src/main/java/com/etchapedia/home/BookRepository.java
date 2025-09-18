package com.etchapedia.home;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {
	Optional<Book> findByIsbn(String isbn);
	
	@Query(value="SELECT * FROM (SELECT * FROM book ORDER BY loan DESC) WHERE ROWNUM <= 10",
			nativeQuery=true)
	List<Book> findTop10ByOrderByLoanDesc();
}
