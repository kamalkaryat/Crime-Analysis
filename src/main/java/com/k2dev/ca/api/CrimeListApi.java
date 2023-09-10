package com.k2dev.ca.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.k2dev.ca.service.CrimeService;

@Controller
@RequestMapping("/crime-api")
public class CrimeListApi {

	@Autowired
	private CrimeService crimeService;
	
	@GetMapping("/crimes")
	public ResponseEntity<?> getCrimes(){
		List<String> crimes= crimeService.getCrimes();
		if(crimes==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching the Crimes");
        return ResponseEntity.ok().body(crimes);
	}
}
