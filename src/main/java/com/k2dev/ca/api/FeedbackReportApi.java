package com.k2dev.ca.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.k2dev.ca.model.FeedbackReportDto;
import com.k2dev.ca.service.FeedbackReportService;
import com.k2dev.ca.util.Constants;

@RestController
@RequestMapping("/report-api")
public class FeedbackReportApi {
	
	@Autowired
	private FeedbackReportService reportService;
	
	//report of all crimes in a region
	@GetMapping("/{pincode}/{areaName}/{from}/{to}")
	public ResponseEntity<?> getAreaReport(@PathVariable int pincode,@PathVariable String areaName,
			@PathVariable String from, @PathVariable String to){
		if(pincode<1)
			pincode= Constants.PINCODE;
		if(areaName==null)
			areaName=Constants.AREA_NAME;
		if(from==null)
			from= Constants.FROM;
		if(to==null)
			to= Constants.TO;
		
		List<List<FeedbackReportDto>> areaReport= reportService.allCrimeBetweenTwoDates(areaName,pincode, from, to);
		if(areaReport==null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while generating the report");
		return ResponseEntity.ok().body(areaReport);
	}
	
	//report of a particular crimes in a region
	@GetMapping("/{pincode}/{areaName}/{from}/{to}/{crime}")
	public ResponseEntity<?> getAreaAndCrimeReport(@PathVariable int pincode,@PathVariable String areaName,
			@PathVariable String from, @PathVariable String to,@PathVariable String  crime){
		if(pincode<1)
			pincode= Constants.PINCODE;
		if(areaName==null)
			areaName=Constants.AREA_NAME;
		if(from==null)
			from= Constants.FROM;
		if(to==null)
			to= Constants.TO;
		if(crime==null)
			crime= Constants.CRIME;
		FeedbackReportDto areaAndCrimeReport= 
				reportService.specificCrimeBetweenTwoDates(areaName, pincode, crime, from, to);
		if(areaAndCrimeReport==null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while generating the report");
		return ResponseEntity.ok().body(areaAndCrimeReport);
	}
	
	//report of all states
	@GetMapping("/states")
	public ResponseEntity<?> allStatesReport(){
		
		return null;
	}
	
	//report of all states
	@GetMapping("/states/{state}")
	public ResponseEntity<?> specificStatesReport(@PathVariable String state){
		
		return null;
	}
		
	//report of specific crime in all state
	@GetMapping("/states/crime/{crime}")
	public ResponseEntity<?> specificCrimeInAllStates(@PathVariable String crime){
		
		return null;
	}
	
	//report of all districts in a state
	@GetMapping("states/{state}/districts")
	public ResponseEntity<?> allDistrictsReport(@PathVariable String state){
		
		return null;
	}
	
	//report of a specific district of a state
	@GetMapping("states/{state}/districts/{district}")
	public ResponseEntity<?> specificDistrictReport(@PathVariable String state, @PathVariable String district){
		
		return null;
	}
		
	//report of specific crime in all districts of a state
	@GetMapping("states/{state}/districts/crimes/{crime}")
	public ResponseEntity<?> specificCrimeInAllDistricts(@PathVariable String state,@PathVariable String crime){
		
		return null;
	}
		
	//report of all pincode
	@GetMapping("states/{state}/districts/{district}/pincode/{pincode}")
	public ResponseEntity<?> allPincode(@PathVariable String state,@PathVariable String district,
			@PathVariable int pincode){
		
		return null;
	}
	
	//report of a specific crime in all pincode
	@GetMapping("states/{state}/districts/{district}/pincode/crimes/{crime}")
	public ResponseEntity<?> all(@PathVariable String state,@PathVariable String district,
			@PathVariable String crime){
		
		return null;
	}
}
