package com.k2dev.ca.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.repository.CrimeRepository;

@Service
public class CrimeServiceImpl implements CrimeService {
	@Autowired
	private CrimeRepository crimeRepository;
	
	@Override
	public List<String> getCrimes() {
		List<Crime> crimes= crimeRepository.findAll();
		return crimes.stream()
				.map(crime-> crime.getCrimeName())
				.collect(Collectors.toList());
	}

}
