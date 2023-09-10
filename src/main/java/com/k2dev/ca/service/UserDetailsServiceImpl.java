package com.k2dev.ca.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.Login;
import com.k2dev.ca.repository.LoginRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginRepository loginRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Login login =loginRepository.findById(username).get();
		Collection<SimpleGrantedAuthority> authorities= new HashSet<>();
		login.getRoles().forEach(role->{
			authorities.add(new SimpleGrantedAuthority(role.getRolename()));
		});
		
//		return new org.springframework.security.core.userdetails.User(login.getUsername(),login.getPassword(),authorities);
		return new MyUserDetails(login);
	}
}
