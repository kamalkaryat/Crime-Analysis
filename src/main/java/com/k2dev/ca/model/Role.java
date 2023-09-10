package com.k2dev.ca.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	@Id
	private int id;
	private String rolename;
//	@Nullable
//	private String rights;
//	
//	@JsonBackReference
//	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "roles")
//	private List<Login> login;
}
