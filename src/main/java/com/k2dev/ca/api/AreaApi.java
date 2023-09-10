package com.k2dev.ca.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.k2dev.ca.service.AreaService;

@RestController
@RequestMapping("/area-api")
public class AreaApi {

	@Autowired
    private AreaService areaService;

    @GetMapping("/states")
    public ResponseEntity<?> findAllStates(){
        List<String> states= areaService.getStates();
        if(states==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching the states");
        return ResponseEntity.ok().body(states);
    }

    @GetMapping("/states/{state}")
    public ResponseEntity<?> findAllDistricts(@PathVariable("state") String state){
        List<String> districts= areaService.getDistricts(state);
        if(districts==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching the districts");
        return ResponseEntity.ok().body(districts);
    }

    @GetMapping("/states/{state}/districts/{district}")
    public ResponseEntity<?> findAllPincodes(@PathVariable("state") String state,@PathVariable("district") String district){
        List<String> pincodes= areaService.getPincodes(state,district);
        if(pincodes==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching the pincodes");
        return ResponseEntity.ok().body(pincodes);
    }

    @GetMapping("/states/state/districts/district/{pincode}")
    public ResponseEntity<?> findAllAreaNames(@PathVariable("pincode") int pincode){
        List<String> areaNames= areaService.getAreaNames(pincode);
        if(areaNames==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching area names");
        return ResponseEntity.ok().body(areaNames);
    }
}
