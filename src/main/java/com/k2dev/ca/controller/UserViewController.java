package com.k2dev.ca.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.FeedbackDto;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.FeedbackReportDto;
import com.k2dev.ca.model.Notification;
import com.k2dev.ca.model.Profile;
import com.k2dev.ca.model.User;
import com.k2dev.ca.service.FeedbackReportService;
import com.k2dev.ca.service.NotificationService;
import com.k2dev.ca.service.UserService;
import com.k2dev.ca.util.Constants;
import com.k2dev.ca.util.PageUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserViewController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private FeedbackReportService reportService;
	
	private static final String username= "niesha.bruch@yahoo.com";
	private static final String userId= "d038e7f3-c62c-40d2-b067-6002ecf7eee8";
	@GetMapping("/home")
	public String home(@ModelAttribute ReportRequest request, Model model) {
		model.addAttribute("title", "Home");
		
		String areaName= Constants.AREA_NAME;
		int pincode= Constants.PINCODE;
		
		//default factors
		if(request.getFrom()==null || request.getTo()==null) {
			if(request.getFrom()==null)
				request.setFrom(Constants.FROM);
			if(request.getTo()==null)
				request.setTo(Constants.TO);
		}
		
		Default defaultInfo= new Default();
		defaultInfo.setCrime(Constants.CRIME);
		defaultInfo.setFrom(request.getFrom());
		defaultInfo.setTo(request.getTo());
		model.addAttribute("default", defaultInfo);
		
		List<List<FeedbackReportDto>> res= reportService.allCrimeBetweenTwoDates(areaName, pincode,
				request.getFrom(), request.getTo());
		model.addAttribute("reports", res.get(0));
		model.addAttribute("increased", res.get(1).get(0));
		model.addAttribute("decreased", res.get(2).get(0));
		return "user_home";
	}	
	
	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("title", "Profile");
		Profile user=  this.userService.getUser(username);
		model.addAttribute("user", user);
		return "profile";
	}
	
	@PostMapping("profile/update")
	public String update(@Valid @ModelAttribute("user") Profile user, BindingResult result, Model model) {
		System.out.println(user);
		if(result.hasErrors()) {
			log.error("Error while processing the user update request");
			return "profile";
		}
		if(this.userService.updateUser(user))
			return "user_home";
		return "profile";
	}
	
	@GetMapping("/feedback/new")
	public String give_feed(Model model) {
		Profile profile= userService.getUser(username);
		FeedbackDto request= new FeedbackDto();
		//set userId
		User user= new User();
		user.setUserId(userId);
		
		request.setUser(user);
		request.setArea(profile.getArea());
		request.setCrime(new Crime());
		model.addAttribute("title", "New Feedback");
		model.addAttribute("feed", request);
		return "new_feedback";
	}
	
	@PostMapping("/submitFeedback")
	public String processFeed(@Valid @ModelAttribute("feed") FeedbackDto feed, BindingResult result, Model model) {
		if(result.hasErrors()) {
			log.error("feedback request has some errors");
			return "new_feedback";
		}
		if(userService.saveFeedback(feed))
			log.info("feedback saved");
		else
			log.error("feedback not saved");
		return "new_feedback";
	}
	
	@GetMapping("/feedbacks/blocked")
	public String blockedFeed(Model model) {
		model.addAttribute("title", "Blocked Feedbacks");
		return viewBlockedFeeds(1, model);
	}
	@GetMapping("/feedbacks/blocked/{pageNo}")
	public String viewBlockedFeeds(@PathVariable int pageNo, Model model) {
		FeedbackPage feedbackPage= userService.findBlockedFeedbacks(userId, pageNo);
		
		model.addAttribute("feedbacks", feedbackPage.getFeedbackDtos());
		PageUtil.setPaginationProperties(model, pageNo, feedbackPage.getPage().getTotalPages(), 
				feedbackPage.getPage().getTotalElements());
		model.addAttribute("title", "Blocked Feedbacks");
		return "view_blocked_feedback";
	}
	
	@GetMapping("/notifications")
	public String notification(Model model) {
		model.addAttribute("title", "Notifications");
		List<Notification> nots= notificationService.getNotifications(userId);
		log.info("Total notifications: "+nots.size());
		model.addAttribute("notifications", nots);
		return "notification";
	}
	@PutMapping("/notifications/{nId}")
	public String deleteNotification(@PathVariable long nId, Model model, HttpServletRequest request) {
		model.addAttribute("title", "Notifications");
		notificationService.deleteNotification(nId);
		log.info("Notification deleted: "+nId);
		model.addAttribute("notifications", notificationService.getNotifications(userId));
		return request.getContextPath()+"/notifications";
	}
	
	@GetMapping("/feedbacks")
	public String feeds(Model model) {
		model.addAttribute("title", "Feedbacks");
		return feedbacks(1, model);
	}
	@GetMapping("/feedback/{pageNo}")
	public String feedbacks(@PathVariable int pageNo, Model model) {
		FeedbackPage feedbackPage= userService.findBlockedFeedbacks(userId, pageNo);
		
		model.addAttribute("feedbacks", feedbackPage.getFeedbackDtos());
		PageUtil.setPaginationProperties(model, pageNo, feedbackPage.getPage().getTotalPages(), 
				feedbackPage.getPage().getTotalElements());
		model.addAttribute("title", "Feedbacks");
		return "view_feedback";
	}
	
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Default{
	String from;
	String to;
	String crime;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class ReportRequest{
	private String from;
	private String to;
	private String crime;
}