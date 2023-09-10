package com.k2dev.ca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
	private String state;
	private long totFeed;
	private double rate;
	private boolean isIncreased;
}
