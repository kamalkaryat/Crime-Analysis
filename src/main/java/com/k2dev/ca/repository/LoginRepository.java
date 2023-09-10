package com.k2dev.ca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.k2dev.ca.model.Login;

public interface LoginRepository extends JpaRepository<Login, String> { }
