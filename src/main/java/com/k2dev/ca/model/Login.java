package com.k2dev.ca.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Login{
	@Id
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private LocalDateTime registeredAt;

	private boolean accNonExpired;
	private boolean accNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Builder.Default
	private Collection<Role> roles= new ArrayList<>();
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator= this.roles.iterator();
		while(iterator.hasNext()) {
			Role role= iterator.next();
			if(role.getRolename().equals(roleName))
				return true;
		}
		return false;
	}
	
}
