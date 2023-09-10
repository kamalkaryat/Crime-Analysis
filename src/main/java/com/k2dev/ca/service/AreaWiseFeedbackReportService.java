package com.k2dev.ca.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.FeedbackReportDto;
import com.k2dev.ca.model.ResultSetDto;
import com.k2dev.ca.repository.FeedbackRepository;
import com.k2dev.ca.util.ResultSetToListMapper;

@Service
public class AreaWiseFeedbackReportService implements FeedbackReportService {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public List<List<FeedbackReportDto>> allCrimeBetweenTwoDates(String areaName,int pincode,String from, String to) {
		//finding total feeds
		List<ResultSetDto> totList= 
				ResultSetToListMapper.convertToResultSetDtoList(feedbackRepository.calculateTotalAreaFeeds(areaName, from, to));
		
		//finding only valid feeds
		Map<String, Long> validList= 
				ResultSetToListMapper.convertToMap(feedbackRepository.calculateValidAreaFeeds(areaName, from, to));
		
		//finding total feedbacks
		Object o= feedbackRepository.findTotalAreaFeeds(pincode, from, to);
		BigInteger bi= (BigInteger)o;
		long totFeed=bi.longValue();
		
		List<List<FeedbackReportDto>> feedbackReportDtos= new ArrayList<>();
		List<FeedbackReportDto> allCrimes= new ArrayList<>();
		List<FeedbackReportDto> increased= new ArrayList<>();
		List<FeedbackReportDto> decreased= new ArrayList<>();
		
		int minInd=0, maxInd=0;
		double min=0.0,max = 0.0;
		for(int i=0; i<totList.size(); i++) {
			
			ResultSetDto dto=totList.get(i);
			FeedbackReportDto report= new FeedbackReportDto();
			report.setTotFeed(dto.getTotFeed());
			report.setCrime(dto.getCrime());
			if(validList.containsKey(dto.getCrime())) {
				report.setValidFeed(validList.get(dto.getCrime()));
				if(totFeed>0) {
					double rate= (double)dto.getTotFeed()*totFeed/100;
					report.setRate(rate);
				}
			}
			else {
				report.setValidFeed(0);
				report.setRate(0.0);
				report.setMargin(0.0);
			}
			if(i==0) 
				min= max= report.getRate();
			
			if(report.getRate()>max) {
				max= report.getRate();
				maxInd= i;
			}
			if(report.getRate()!=0.0 && min>report.getRate()) {
				min= report.getRate();
				minInd= i;
			}
			allCrimes.add(report);
		}
		increased.add(allCrimes.get(maxInd));
		decreased.add(allCrimes.get(minInd));
		
		feedbackReportDtos.add(allCrimes);
		feedbackReportDtos.add(increased);
		feedbackReportDtos.add(decreased);
		return feedbackReportDtos;
	}

	@Override
	public FeedbackReportDto specificCrimeBetweenTwoDates(String areaName,int pincode,String crime, String from, String to) {
		return null;
	}

}
