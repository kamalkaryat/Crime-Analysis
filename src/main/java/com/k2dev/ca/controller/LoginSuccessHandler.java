package com.k2dev.ca.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.k2dev.ca.service.MyUserDetails;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		log.info("success handler: called");
		MyUserDetails userDetails= (MyUserDetails) authentication.getPrincipal();
		String redirectUrl= request.getContextPath();
		if(userDetails.hasRole("USER"))
			redirectUrl+= "/user/home";
		else if(userDetails.hasRole("ADMIN"))
			redirectUrl+= "/admin/home";
		response.sendRedirect(redirectUrl);
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
}
