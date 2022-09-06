package com.talentica.articles.security.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByUsername(String username);

	List<User> findByRole(Role role);
}
