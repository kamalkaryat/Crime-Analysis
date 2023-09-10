package com.k2dev.ca.service;

import java.util.List;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.CrimePage;
import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackDto;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.Profile;
import com.k2dev.ca.model.User;
import com.k2dev.ca.model.UserPage;

public interface AdminService {
	Profile getProfile(String userId);
	boolean updateAdmin(Profile profile);
	
	boolean blockUsers(List<String> users);
	boolean unblockUsers(List<String> users);
	
	boolean blockFeedbacks(List<String> feedbacks);
	boolean unblockFeedbacks(List<String> feedbacks);
	
	boolean addCrime(Crime crime);
	
	boolean removeCrime(String crimeId);
	boolean addAdmin(User user);
	
	FeedbackPage getAllActiveFeedbacks(int pageNo);
	FeedbackPage getAllBlockedFeedbacks(int pageNo);	
	
	UserPage getAllActiveUsers(int pageNo);
	UserPage getAllNonActiveUsers(int pageNo);
	
	CrimePage getAllCrimes(int pageNo);
}
