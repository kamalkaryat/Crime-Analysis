package com.k2dev.ca.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackDto;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.Login;
import com.k2dev.ca.model.Profile;
import com.k2dev.ca.model.User;
import com.k2dev.ca.repository.CrimeRepository;
import com.k2dev.ca.repository.FeedbackRepository;
import com.k2dev.ca.repository.UserRepository;
import com.k2dev.ca.util.ModelCloneUtil;
import com.k2dev.ca.util.PageUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CrimeRepository crimeRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Override
	public Profile getUser(String username) {
		User user= this.userRepository.findByLoginUsername(username);
		Profile profile= new Profile();
		profile.setUserId(user.getUserId());
		profile.setName(user.getName());
		profile.setGender(user.getGender());
		profile.setDob(user.getDob().toString());
		profile.setOccupation(user.getOccupation());
		profile.setPhone(user.getPhone());
		profile.setArea(user.getArea());
		profile.setUsername(user.getLogin().getUsername());
		return profile;
	}

	@Override
	public boolean updateUser(Profile profile) {
		User user= new User();
		user.setUserId(profile.getUserId());
		user.setName(profile.getName());
		user.setGender(profile.getGender());
		user.setDob(LocalDate.parse(profile.getDob()));
		user.setPhone(profile.getPhone());
		user.setOccupation(profile.getOccupation());
		user.setArea(profile.getArea());
		Login login= new Login();
		login.setUsername(profile.getUsername());
		user.setLogin(login);
		User res= userRepository.save(user);
		return res!=null? true : false;
	}

	@Override
	public boolean saveFeedback(FeedbackDto request) {
		Feedback feedback= new Feedback();
		feedback.setFeedId(UUID.randomUUID().toString());
		DateTimeFormatter f= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		feedback.setFeedDateTime(LocalDateTime.parse(request.getFeedDateTime(), f));
		feedback.setUser(request.getUser());
		Crime c= crimeRepository.findByCrimeName(request.getCrime().getCrimeName());
		feedback.setCrime(c);
		feedback.setActive(true);
		Feedback res= feedbackRepository.save(feedback);
		if(res!=null)
			return true;
		return false;
	}

	@Override
	public FeedbackPage findActiveFeedbacks(String userId, int pageNo) {
		log.info("service: finalActiveFeedbacks");
		Page<Feedback> page= feedbackRepository.findAllByUserUserIdAndIsActive(
				userId,true,PageUtil.pageable(pageNo));
		return new FeedbackPage(ModelCloneUtil.copyDataInModel(page.getContent()), page);
	}

	@Override
	public FeedbackPage findBlockedFeedbacks(String userId, int pageNo) {
		log.info("service: blockedFeedback");
		Page<Feedback> page= feedbackRepository.findAllByUserUserIdAndIsActive(
				userId,false,PageUtil.pageable(pageNo));
		return new FeedbackPage(ModelCloneUtil.copyDataInModel(page.getContent()), page);
	}
}