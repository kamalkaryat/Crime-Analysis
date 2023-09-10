package com.k2dev.ca.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.CrimePage;
import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackDto;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.Login;
import com.k2dev.ca.model.Notification;
import com.k2dev.ca.model.Profile;
import com.k2dev.ca.model.User;
import com.k2dev.ca.model.UserPage;
import com.k2dev.ca.repository.CrimeRepository;
import com.k2dev.ca.repository.FeedbackRepository;
import com.k2dev.ca.repository.LoginRepository;
import com.k2dev.ca.repository.NotificationRepository;
import com.k2dev.ca.repository.UserRepository;
import com.k2dev.ca.util.ModelCloneUtil;
import com.k2dev.ca.util.PageUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private CrimeRepository crimeRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public Profile getProfile(String userId) {
		User user= this.userRepository.findById(userId).get();
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

	@Transactional
	@Override
	public boolean updateAdmin(Profile profile) {
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
	
	@Transactional
	@Override
	public boolean blockFeedbacks(List<String> blockFeeds) {
		List<Feedback> feedbacks= feedbackRepository.findAllById(blockFeeds);
		List<Notification> notifications= new ArrayList<>();
		for(Feedback f: feedbacks) {
			f.setActive(false);
			Notification notification= new Notification();
			notification.setId(0);
			notification.setTitle("Feedback Blocked");
			notification.setNotfdesc(f.getFeedId()+" this feedback submitted for "+f.getCrime().getCrimeName()
					+" on "+f.getFeedDateTime()+" has been blocked by the admin");
			notification.setGeneratedAt(LocalDateTime.now());
			notification.setUserId(f.getUser().getUserId());
			notifications.add(notification);
		}
		notificationRepository.saveAll(notifications);
		return feedbackRepository.saveAll(feedbacks)!=null;
	}

	@Transactional
	@Override
	public boolean unblockFeedbacks(List<String> blockFeeds) {
		List<Feedback> feedbacks= feedbackRepository.findAllById(blockFeeds);
		List<Notification> notifications= new ArrayList<>();
		for(Feedback f: feedbacks) {
			f.setActive(true);
			Notification notification= new Notification();
			notification.setId(0);
			notification.setTitle("Feedback Unblocked");
			notification.setNotfdesc(f.getFeedId()+" this feedback submitted for "+f.getCrime().getCrimeName()
					+" on "+f.getFeedDateTime()+" has been unblocked by the admin");
			notification.setGeneratedAt(LocalDateTime.now());
			notification.setUserId(f.getUser().getUserId());
			notifications.add(notification);
		}
		notificationRepository.saveAll(notifications);
		return feedbackRepository.saveAll(feedbacks)!=null;
	}
	
	@Transactional
	@Override
	public boolean addCrime(Crime crime) {
		if(crimeRepository.findByCrimeName(crime.getCrimeName()) != null) {
			log.error("This crime already exists: "+crime.getCrimeName());
			return false;
		}
		List<Crime> crimes= crimeRepository.findAll();
		crime.setCrimeId("C"+(crimes.size()+1));
		return crimeRepository.save(crime)!=null;
	}

	@Transactional
	@Override
	public boolean removeCrime(String crimeId) {
		crimeRepository.deleteById(crimeId);
		return true;
	}

	@Transactional
	@Override
	public boolean addAdmin(User user) {
		return userRepository.save(user)!=null;
	}

	@Transactional
	@Override
	public boolean blockUsers(List<String> usersId) {
		List<User> users= userRepository.findAllById(usersId);
		List<String> logins = new ArrayList<>();
		List<Notification> notifications= new ArrayList<>();
		for(User user: users ) {
			logins.add(user.getLogin().getUsername());
			Notification notification= new Notification();
			notification.setTitle("User Blocked");
			notification.setNotfdesc(user.getLogin().getUsername()
					+" this user id has been blocked by the admin on "+LocalDate.now());
			notification.setGeneratedAt(LocalDateTime.now());
			notification.setUserId(user.getUserId());
			notifications.add(notification);
		}
		List<Login> lg= loginRepository.findAllById(logins);
		for(Login login: lg)
			login.setEnabled(false);

		List<Login> res= loginRepository.saveAll(lg);
		if(res!=null)
			return true;
		return false;
	}
	
	@Transactional
	@Override
	public boolean unblockUsers(List<String> usersId) {
		List<User> users= userRepository.findAllById(usersId);
		List<String> logins = new ArrayList<>();
		List<Notification> notifications= new ArrayList<>();
		for(User user: users ) {
			logins.add(user.getLogin().getUsername());
			Notification notification= new Notification();
			notification.setTitle("User Unblocked");
			notification.setNotfdesc(user.getLogin().getUsername()
					+" this user id has been unblocked by the admin on "+LocalDate.now());
			notification.setGeneratedAt(LocalDateTime.now());
			notification.setUserId(user.getUserId());
			notifications.add(notification);
		}
		List<Login> lg= loginRepository.findAllById(logins);
		for(Login login: lg)
			login.setEnabled(true);
		return loginRepository.saveAll(lg)!=null;
	}
	
	public FeedbackPage getAllBlockedFeedbacks(int pageNo){
		Page<Feedback> page= feedbackRepository.findAllByIsActiveFalse((PageUtil.pageable(pageNo)));
		return new FeedbackPage(ModelCloneUtil.copyDataInModel(page.getContent()), page);	
	}
	
	public FeedbackPage getAllActiveFeedbacks(int pageNo){
		Page<Feedback> page= feedbackRepository.findAllByIsActiveTrue((PageUtil.pageable(pageNo)));
		return new FeedbackPage(ModelCloneUtil.copyDataInModel(page.getContent()), page);	
	}
	

	@Override
	public UserPage getAllActiveUsers(int pageNo) {
		Page<User> page= userRepository.findAllByLoginEnabledTrue(PageUtil.pageable(pageNo));
		return new UserPage(page.getContent(), page);	
	}

	@Override
	public UserPage getAllNonActiveUsers(int pageNo) {
		Page<User> page= userRepository.findAllByLoginEnabledFalse(PageUtil.pageable(pageNo));
		return new UserPage(page.getContent(), page);
	}

	@Override
	public CrimePage getAllCrimes(int pageNo) {
		Page<Crime> page= crimeRepository.findAll(PageUtil.pageable(pageNo));
		return new CrimePage(page.getContent(), page);
	}
	
}
