package com.k2dev.ca.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
	
	private static String CTX_PATH;
	private RedirectStrategy redirectStrategy=
			new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CTX_PATH= request.getContextPath();
		log.info("success handler is working");
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		
		String targetUrl= determineTargetUrl(authentication);
		if(response.isCommitted()) {
			log.debug("Response has already been committed, unable to redirect to: "+targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	protected String determineTargetUrl(final Authentication authentication) {
		Map<String, String> roleTargetUrlMap= new HashMap<>();
		roleTargetUrlMap.put("USER", CTX_PATH+"/user/home");
		roleTargetUrlMap.put("ADMIN", CTX_PATH+"/admin/home");
		final Collection<? extends GrantedAuthority> authorities= 
				authentication.getAuthorities();
		for(final GrantedAuthority grantedAuthority : authorities) {
			String authorityName= grantedAuthority.getAuthority();
			if(roleTargetUrlMap.containsKey(authorityName))
				return roleTargetUrlMap.get(authorityName);
		}
		throw new IllegalArgumentException();
	}
	
	protected void  clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session= request.getSession(false);
		if(session==null)
			return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
