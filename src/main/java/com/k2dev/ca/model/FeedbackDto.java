package com.k2dev.ca.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
	
	private User user;
	@NotEmpty(message = "Feedback Date & Time can't be empty")
	private String feedDateTime;
	private Crime crime;
	private Area area;
	private String feedId;
}
