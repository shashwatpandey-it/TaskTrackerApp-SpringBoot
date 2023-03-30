package com.tasktrackerapplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tasktrackerapplication.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findUserByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
