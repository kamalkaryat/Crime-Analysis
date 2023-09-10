package com.k2dev.ca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class FeedbackReportDto {
	private String crime;
	private long totFeed;
	private long validFeed;
	private double rate;
	private double margin;
}
