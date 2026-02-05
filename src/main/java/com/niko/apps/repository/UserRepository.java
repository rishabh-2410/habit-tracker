package com.niko.apps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niko.apps.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
}
