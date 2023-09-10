package com.k2dev.ca.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Profile {
	
	private String userId;
	private Name name;
	@NotBlank(message = "Dob can't be empty")
	private String dob;
	private String gender;
	@Size(min = 10, max = 10, message = "Phone No must contain 10 digits")
	private String phone;
	@Size(max = 20, message = "Occupation length must be less than 21 characters")
	private String occupation;
	private String username;
	private Area area;
}
