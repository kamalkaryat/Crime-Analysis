package com.k2dev.ca.model;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignupRequest {
	
	@NotBlank(message ="Username can't be blank")
	@Email(message = "Invalid username")
	private String username;
	private Name name;
	
	@NotBlank(message = "Dob can't be empty")
	private String dob;
	private String gender;
	
	@Size(min = 10, max = 10, message = "Phone No must contain 10 digits")
	private String phone;
	@Size(max = 20, message = "Occupation length must be less than 21 characters")
	private String occupation;
	private Area area;
	
	@NotBlank(message ="Password can't be empty")
	@Size(min = 6, max = 15, message = "Password must contain 6-15 characters")
	private String psw;
	@NotBlank(message ="Confirm Password can't be empty")
	@Size(min = 6, max = 15, message = "Confirm Password must contain 6-15 characters")
	private String confirmPsw;
}
