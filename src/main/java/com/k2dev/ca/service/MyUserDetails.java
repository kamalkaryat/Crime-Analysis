package com.k2dev.ca.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.k2dev.ca.model.Login;
import com.k2dev.ca.model.Role;

public class MyUserDetails implements UserDetails{
	private static final long serialVersionUID = 1L;
	private Login user;
	private String username;
	private String password;
	private Collection<SimpleGrantedAuthority> authorities= new HashSet<>();
	private boolean isEnabled;
	private boolean isAccountNonLocked;
	private boolean isAccountNonExpired;
	private boolean isCredentialsNonExpired;
	
	public MyUserDetails(Login user) {
		this.username = user.getUsername();
		this.password= user.getPassword();
		this.isAccountNonExpired= user.isAccNonExpired();
		this.isAccountNonLocked= user.isAccNonLocked();
		this.isCredentialsNonExpired= user.isCredentialsNonExpired();
		this.isEnabled= user.isEnabled();
		user.getRoles().stream().forEach(role->{
			authorities.add(new SimpleGrantedAuthority(role.getRolename()));
		});
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return authorities;
	}
	
	public boolean hasRole(String roleName) {
		return this.user.hasRole(roleName);
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
