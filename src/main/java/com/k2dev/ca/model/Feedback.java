package com.k2dev.ca.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="feedback")
public class Feedback {
	@Id
	@NotNull
	private String feedId;
	@NotNull(message = "Feedback data-time can't be empty")
	private LocalDateTime feedDateTime;
	
	@OneToOne
	@NotNull
	private Crime crime;
	@ManyToOne
	@NotNull
	private User user;
	
	private boolean isActive;
}
