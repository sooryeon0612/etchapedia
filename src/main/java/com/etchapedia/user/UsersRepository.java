package com.etchapedia.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;

@Repository
>>>>>>> refs/heads/testSR
public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String email);
}
