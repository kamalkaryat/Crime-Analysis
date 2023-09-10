package com.k2dev.ca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.k2dev.ca.model.Crime;

public interface CrimeRepository extends JpaRepository<Crime, String>{
	Crime findByCrimeName(String crimeName);
}
