package com.k2dev.ca.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.k2dev.ca.exceptions.PasswordNotMatchedException;
import com.k2dev.ca.exceptions.UserExistsException;
import com.k2dev.ca.model.Area;
import com.k2dev.ca.model.AuthenticationRequest;
import com.k2dev.ca.model.ForgotPswRequest;
import com.k2dev.ca.model.Name;
import com.k2dev.ca.model.SignupRequest;
import com.k2dev.ca.model.User;
import com.k2dev.ca.service.LoginServiceImp;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginViewController {
	
	@Autowired
	private LoginServiceImp loginService;
	
	@GetMapping("/")
	public String indexHandler(HttpServletRequest request) {
//		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
//		if(!(auth instanceof AnonymousAuthenticationToken)) {
//			return request.getContextPath()+"/admin/home";
//		}
		return "index";
	} 
	
	@GetMapping("/login")
	public String loginHandler(Model model, HttpServletRequest request) {
		model.addAttribute("title","Login");
		model.addAttribute("login", new AuthenticationRequest());
		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		System.out.println("is auth null? ="+auth.getPrincipal());
		if(!(auth instanceof AnonymousAuthenticationToken)) {
			System.out.println("authentication res: "+(auth instanceof AnonymousAuthenticationToken));
			return "redirect:"+request.getContextPath()+"/admin/home";
		}
		return "login";
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public String authProcessorHandler(@Valid @ModelAttribute("login") AuthenticationRequest login,
			BindingResult result,Model model, HttpServletRequest request) {
		
		String ctxPath= request.getContextPath();
		
		if(result.hasErrors()) {
			log.error("Validation failed");
			model.addAttribute("login", login);
			model.addAttribute("error_msg", "incorrect credentials");
			return "login";
		}	
		boolean isLoginSuccessfully= loginService.authentication(login);
//		if(authResult.equalsIgnoreCase("BadCredentialsException") || 
//				authResult.equalsIgnoreCase("LockedException") ||
//				authResult.equalsIgnoreCase("DisabledException")) {
//			if(authResult.equalsIgnoreCase("BadCredentialsException"))
//				model.addAttribute("error_msg", "Invalid Credentials");
//
//			else if(authResult.equalsIgnoreCase("LockedExceptionn"))
//				model.addAttribute("error_msg", "Account is Locked");
//			else if(authResult.equalsIgnoreCase("DisabledException"))
//				model.addAttribute("error_msg", "Account is Disabled");
//			else
//				model.addAttribute("error_msg", "Error while signup");
//			return "login";	
//		}
		
//		boolean isUser= true;
//		Collection<? extends GrantedAuthority> roles= userDetails.getAuthorities();
//		for(GrantedAuthority role: roles) {
//			String a= role.getAuthority();
//			log.info(a);
//			if(!a.equals("USER"))
//				isUser= false;
//		}
		//return ctxPath+"/user/home";
//		return isUser ? "redirect:"+ctxPath+"/user/home" : "redirect:"+ctxPath+"/admin/home";	
		if(isLoginSuccessfully) {
			System.out.println("LoginHandler: Returning the view");
			return "redirect:"+ctxPath+"/admin/home";
		}
		else {
			System.out.println("Login Failded");
			return "login";
		}
	}
	
	
	@GetMapping("/signup")
	public String signupHandler(Model model) {
		model.addAttribute("title","SignUp");
		SignupRequest user= new SignupRequest();
		user.setArea(new Area());
		user.setName(new Name());
		model.addAttribute("user", user);
		return "signup";
	}
	
	@PostMapping("/signup/add")
	public String newUser(@Valid @ModelAttribute("user") SignupRequest user, 
			BindingResult result, Model model, HttpServletRequest request) {
		String ctxPath= request.getContextPath();
		if(result.hasErrors()) {
			log.error("Error while processing the signup request");
			model.addAttribute("user", user);
			model.addAttribute("error_msg", "Error in signup");	
			return "signup";
		}
		User res = null;
		try {
			res= loginService.saveUser(user);
		}catch (PasswordNotMatchedException e) {
			model.addAttribute("error_msg", "Password & Confirm Password must be same");			
			return "signup";
		}
		catch (UserExistsException e) {
			model.addAttribute("error_msg", "User already exists");			
			return "signup";
		}
		catch (Exception e) {
			model.addAttribute("error_msg", "Error in signup");			
			return "signup";
		}
		if(res==null) {
			model.addAttribute("error_msg", "Error in signup");			
			return "signup";			
		}
		return "redirect:"+ctxPath+"/login";
	}
	
	@GetMapping("/forgotPsw")
	public String forgotPsw(Model model) {
		model.addAttribute("title", "ForgotPassword");
		return "forgotPsw";
	}
	
	@RequestMapping(value = "/forgotPswProcessor", method= RequestMethod.POST)
	public String forgotPswProcessor(@RequestParam String username, Model model) {
		if(!loginService.isExists(username))
			return "redirect:forgotPsw";
		ForgotPswRequest request= new ForgotPswRequest();
		request.setUsername(username);
		model.addAttribute("forgotPswRequest", request);
		return "resetPsw";
	}
	
	@GetMapping("/resetPsw")
	public String resetPsw(Model model) {
		model.addAttribute("title", "ResetPassword");
		return "resetPsw";
	}
	
	@PostMapping("/resetPswProcessor")
	public String resetPswProcessor(@ModelAttribute ForgotPswRequest request, Model model) {
		boolean res= loginService.resetPsw((String)model.getAttribute("username"), request);
		if(res)
			return "redirect:/login";
		return "resetPsw";
	}
	
	@GetMapping("/generateFeed")
	public String genderateFeed() {
		loginService.generateFeed();
		return "index";
	}
	
}