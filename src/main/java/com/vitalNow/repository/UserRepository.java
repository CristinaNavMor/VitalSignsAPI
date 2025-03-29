package com.vitalNow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitalNow.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	
	Optional<User> findByUsername(String username);
	
	List<User> findAll();
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
	
}
