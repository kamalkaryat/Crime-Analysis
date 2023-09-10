package com.k2dev.ca.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackDto;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.Profile;

public interface UserService {
	Profile getUser(String username);
	boolean updateUser(Profile user);
	boolean saveFeedback(FeedbackDto request);
	FeedbackPage findActiveFeedbacks(String username, int pageNo);
	FeedbackPage findBlockedFeedbacks(String userId, int pageNo);
}
