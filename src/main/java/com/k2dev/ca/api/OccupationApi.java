package com.k2dev.ca.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/occupation-api")
public class OccupationApi {
	
	
	@GetMapping("/occupations")
	public ResponseEntity<?> getOccupations(){
		List<String> occupList= new ArrayList<>();
		occupList.add("Student");
		occupList.add("Teacher");
		occupList.add("Defnce Personel");
		occupList.add("Govt Employee");
		occupList.add("Doctor");
		occupList.add("Lawyer");
		occupList.add("Nurse");
		occupList.add("Private Sector Employee");
		occupList.add("Social Worker");
        return ResponseEntity.ok().body(occupList);
	}
}
