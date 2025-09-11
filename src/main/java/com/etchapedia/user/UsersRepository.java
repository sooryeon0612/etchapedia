package com.etchapedia.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String email);
}
