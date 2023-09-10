package com.k2dev.ca.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "areas")
public class Area {
	
	@NotBlank(message = "State/UT can't be empty")
	private String state;
	@NotBlank(message = "District/City can't be empty")
	private String district;
	@Range(min=111111, max=999999, message = "Pincode can't be empty")
    private int pincode;
	@NotBlank(message = "Area Name can't be empty")
    private String areaName;
    @Id
    private int areaId;
    
    
    @JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private List<User> users;
}
