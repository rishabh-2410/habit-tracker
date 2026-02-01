package com.niko.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niko.apps.controllers.auth.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
