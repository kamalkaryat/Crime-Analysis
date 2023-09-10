package com.k2dev.ca.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name="users")
public class User {

	@Id
	@Column(unique = true)
	private String userId;
	
	@Embedded
	private Name name;
	private LocalDate dob;
	private String gender;
	private String phone;
	private String occupation;
	
	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL)
	private Login login;
	
	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL)
	private Area area;

}
