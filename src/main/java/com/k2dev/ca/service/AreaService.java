package com.k2dev.ca.service;

import java.util.List;

public interface AreaService {
	List<String> getStates();
	List<String> getDistricts(String state);
	List<String> getPincodes(String state, String district);
	List<String> getAreaNames(int pincode);
}
