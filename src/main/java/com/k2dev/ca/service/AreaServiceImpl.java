package com.k2dev.ca.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.k2dev.ca.repository.AreaRepository;

@Service
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	public List<String> getStates() {
		return areaRepository.findByDistinctState();
	}

	@Override
	public List<String> getDistricts(String state) {
		return areaRepository.findDistinctDistrictByState(state);
	}

	@Override
	public List<String> getPincodes(String state, String district) {
		List<Integer> pincodes= areaRepository.findDistinctByStateAndDistrict(state, district);
		List<String> pins= new ArrayList<String>();
		for(int pinocde: pincodes)
			pins.add(String.valueOf(pinocde));
		return pins;
	}

	@Override
	public List<String> getAreaNames(int pincode) {
		return areaRepository.findDistinctAreaNameByPincode(pincode);
	}

}
