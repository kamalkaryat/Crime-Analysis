package com.k2dev.ca.service;

import com.k2dev.ca.model.AuthenticationRequest;
import com.k2dev.ca.model.SignupRequest;
import com.k2dev.ca.model.User;

public interface LoginService {
	boolean authentication(AuthenticationRequest request);
	User getUser(String username);
	User saveUser(SignupRequest request);
	boolean isExists(String username);
}
