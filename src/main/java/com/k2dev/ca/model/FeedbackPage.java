package com.k2dev.ca.model;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackPage {
	private List<FeedbackDto> feedbackDtos;
	private Page<Feedback> page;
}
