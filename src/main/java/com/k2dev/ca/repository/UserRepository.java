package com.k2dev.ca.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.k2dev.ca.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	@Query("select u from User u where u.login.username=:username")
	User findByLoginUsername(@Param("username") String username);
	Page<User> findAllByLoginEnabledTrue(Pageable pageable);
	Page<User> findAllByLoginEnabledFalse(Pageable pageable);
}

