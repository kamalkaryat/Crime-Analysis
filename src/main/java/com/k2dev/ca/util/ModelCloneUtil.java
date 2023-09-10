package com.k2dev.ca.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackDto;

public class ModelCloneUtil {
	public static List<FeedbackDto> copyDataInModel(List<Feedback> feeds){
		List<FeedbackDto> feedbackDtos = new ArrayList<>();
		for(Feedback feedback: feeds) {
			FeedbackDto feedbackDto= new FeedbackDto();
			feedbackDto.setArea(feedback.getUser().getArea());
			feedbackDto.setCrime(feedback.getCrime());
//			LocalDateTime d = LocalDateTime.parse(feedback.getFeedDateTime().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));                                                                                                                                                                                     
//            System.out.println("format:   " + d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			feedbackDto.setFeedDateTime(feedback.getFeedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			
            feedbackDto.setFeedId(feedback.getFeedId());
			feedbackDto.setUser(feedback.getUser());
			feedbackDtos.add(feedbackDto);
		}
		return feedbackDtos;
	}
}
