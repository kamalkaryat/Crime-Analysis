package com.k2dev.ca.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationRequest {
	@NotBlank(message = "Username can't be empty")
	@Email(message = "Invalid username")
	private String username;
	@NotBlank(message = "Password can't be empty")
	private String password;
	private boolean remember;
}
