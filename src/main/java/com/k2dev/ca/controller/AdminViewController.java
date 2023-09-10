package com.k2dev.ca.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.CrimePage;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.Profile;
import com.k2dev.ca.model.User;
import com.k2dev.ca.model.UserPage;
import com.k2dev.ca.repository.CrimeRepository;
import com.k2dev.ca.service.AdminService;
import com.k2dev.ca.service.UserService;
import com.k2dev.ca.util.PageUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminViewController {

	@Autowired
	private AdminService adminService;
	
	private static final String username= "dev1@gmail.com";
	private static final String userId= "123456";
	private final String FEEDBACKS= "/admin/feedbacks";
	private final String USERS= "/admin/users";
	
	@GetMapping("/home")
	public String adminHomeView() {
		return "admin_home";
	}
	
	@GetMapping("/profile")
	public String profileView(Model model) {
		model.addAttribute("title", "Profile");
		model.addAttribute("admin", adminService.getProfile(userId));
		return "admin_profile";
	}
	
	@PostMapping("profile/update")
	public String update(@Valid @ModelAttribute("admin") Profile admin, BindingResult result, Model model,
			HttpServletRequest request) {
		if(result.hasErrors()) {
			log.error("Error while processing the user update request");
			return "admin_profile";
		}
		if(this.adminService.updateAdmin(admin))
			return "redirect:"+request.getContextPath()+"/admin/home";
		return "admin_profile";
	}
	
	@GetMapping("/add")
	public String addAdminView(Model model) {
		model.addAttribute("title", "Add Admin");
		return "add_admin";
	}
	
	@GetMapping("/users")
	public String usersView(Model model) {
		model.addAttribute("title", "Users");
		return usersPage(1, model);
	}
	@GetMapping("/users/{pageNo}")
	public String usersPage(@PathVariable int pageNo, Model model) {
		UserPage page= adminService.getAllActiveUsers(pageNo);
		model.addAttribute("users", page.getUsers());
		PageUtil.setPaginationProperties(model, pageNo, page.getPage().getTotalPages(), 
				page.getPage().getTotalElements());
		model.addAttribute("title", "Users");
		return "users";
	}
	
	@GetMapping("/users/blocked")
	public String blockedUsers(Model model) {
		model.addAttribute("title", "Blocked Users");
		return blockedUsers(1, model);
	}
	@GetMapping("/users/blocked/{pageNo}")
	public String blockedUsers(@PathVariable int pageNo, Model model) {
		model.addAttribute("title", "Blocked Users");
		UserPage page= adminService.getAllNonActiveUsers(pageNo);
		
		model.addAttribute("users", page.getUsers());
		PageUtil.setPaginationProperties(model, pageNo, page.getPage().getTotalPages(), 
				page.getPage().getTotalElements());
		return "blocked_users";
	}
	
	@GetMapping("/feedbacks")
	public String feedbacksView(Model model) {
		model.addAttribute("title", "Feedbacks");
		return feedbacks(1, model);
	}
	@GetMapping("/feedbacks/{pageNo}")
	public String feedbacks(@PathVariable int pageNo, Model model) {
		FeedbackPage feedbackPage= adminService.getAllActiveFeedbacks(pageNo);
		model.addAttribute("feedbacks", feedbackPage.getFeedbackDtos());
		PageUtil.setPaginationProperties(model, pageNo, feedbackPage.getPage().getTotalPages(), 
				feedbackPage.getPage().getTotalElements());
		model.addAttribute("title", "Feedbacks");
		return "feedbacks";
	}
	
	@GetMapping("/feedbacks/blocked")
	public String blockedFeedbacksView(Model model) {
		model.addAttribute("title", "Blocked Feedbacks");
		//return first page
		return blockedFeedbacks(1, model);
	}
	@GetMapping("/feedbacks/blocked/{pageNo}")
	public String blockedFeedbacks(@PathVariable int pageNo, Model model) {
		FeedbackPage page= adminService.getAllBlockedFeedbacks(pageNo);
		
		model.addAttribute("feedbacks", page.getFeedbackDtos());
		PageUtil.setPaginationProperties(model, pageNo, page.getPage().getTotalPages(), 
				page.getPage().getTotalElements());
		model.addAttribute("title", "Blocked Feedbacks");
		return "blocked_feedbacks";
	}
	
	@GetMapping("/users/user/block/{userId}")
	public String blockUser(@PathVariable String userId, HttpServletRequest request) {
		if(adminService.blockUsers(List.of(userId)))
			log.info("User(s) has been successfylly blocked");
		else
			log.info("User(s) has not been blocked blocked");
		return "redirect:"+request.getContextPath()+USERS;
	}
	@GetMapping("/users/user/unblock/{userId}")
	public String unblockUser(@PathVariable String userId, HttpServletRequest request) {
		if(adminService.unblockUsers(List.of(userId)))
			log.info("User(s) has been successfylly unblocked");
		else
			log.info("User(s) has not been blocked unblocked");
		return "redirect:"+request.getContextPath()+USERS;
	}
	
	@GetMapping("/feedbacks/feedback/block/{fId}")
	public String blockFeed(@PathVariable String fId, HttpServletRequest request) {
		if(adminService.blockFeedbacks(List.of(fId)))
			log.info("Feedback(s) has been successfylly blocked");
		else
			log.info("Feedback(s) has not been blocked");
		return "redirect:"+request.getContextPath()+FEEDBACKS;
	}
	
	@GetMapping("/feedbacks/feedback/unblock/{fId}")
	public String unblockFeed(@PathVariable String fId, HttpServletRequest request) {		
		if(adminService.unblockFeedbacks(List.of(fId)))
			log.info("Feedback(s) has been successfylly unblocked");
		else
			log.info("Feedback(s) has not been blocked unblocked");
		return "redirect:"+request.getContextPath()+FEEDBACKS;
	}
	
	@PostMapping("/save_admin")
	public void addAdminView(@ModelAttribute User user) {
		if(adminService.addAdmin(user))
			log.info("Admin added successfully");
		else
			log.info("Error while adding admin");
	}
	
	@GetMapping("/crimes/new_crime")
	public String addCrimeView(Model model) {
		model.addAttribute("title", "Add Crime");
		model.addAttribute("crime", new Crime());
		return "new_crime";
	}
	
	@PostMapping("/crimes/add")
	public String addCrime(@Valid @ModelAttribute Crime crime,BindingResult bindingResult, HttpServletRequest request) {
		if(bindingResult.hasErrors()) {
			return "new_crime";
		}
		if(!adminService.addCrime(crime)) {
			log.error("Crime Not added");
			bindingResult.reject("crimeName",null,"Crime already exists");
			return "new_crime";
		}
		return "new_crime";
	}
	
	@PostMapping("/crimes/remove/{crimeId}")
	public String addCrime(@PathVariable String crimeId, HttpServletRequest request) {
		if(!adminService.removeCrime(crimeId)) {
			log.info("Crime Not added");
			
		}
		return "redirect:"+request.getContextPath()+"/admin/crimes";
	}
	
	@GetMapping("/crimes")
	public String crimeView(Model model) {
		model.addAttribute("title", "Crimes");
		return crimePage(1, model);
	}
	
	@GetMapping("/crimes/{pageNo}")
	public String crimePage(@PathVariable int pageNo, Model model) {
		CrimePage page= adminService.getAllCrimes(pageNo);
		model.addAttribute("crimes", page.getCrimes());
		PageUtil.setPaginationProperties(model, pageNo, page.getPage().getTotalPages(), 
				page.getPage().getTotalElements());
		model.addAttribute("title", "Crimes");
		return "crimes";
	}
}
