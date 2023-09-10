package com.k2dev.ca.service;

import java.util.List;

import com.k2dev.ca.model.FeedbackReportDto;

public interface FeedbackReportService {
	List<List<FeedbackReportDto>> allCrimeBetweenTwoDates(String region,int pincode,String from , String to);
	FeedbackReportDto specificCrimeBetweenTwoDates(String region,int pincode,String crime,String from, String to); 
}
