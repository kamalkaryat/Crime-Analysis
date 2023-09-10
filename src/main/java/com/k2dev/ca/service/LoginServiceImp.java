package com.k2dev.ca.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.k2dev.ca.exceptions.AreaNotExistsException;
import com.k2dev.ca.exceptions.PasswordNotMatchedException;
import com.k2dev.ca.exceptions.UserExistsException;
import com.k2dev.ca.model.AuthenticationRequest;
import com.k2dev.ca.model.Crime;
import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.ForgotPswRequest;
import com.k2dev.ca.model.Login;
import com.k2dev.ca.model.Name;
import com.k2dev.ca.model.Role;
import com.k2dev.ca.model.SignupRequest;
import com.k2dev.ca.model.User;
import com.k2dev.ca.repository.AreaRepository;
import com.k2dev.ca.repository.CrimeRepository;
import com.k2dev.ca.repository.FeedbackRepository;
import com.k2dev.ca.repository.LoginRepository;
import com.k2dev.ca.repository.RoleRepository;
import com.k2dev.ca.repository.UserRepository;
import com.k2dev.ca.util.JwtUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImp implements LoginService{

	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CrimeRepository crimeRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Transactional
	@Override
	public User saveUser(SignupRequest request) {
		if(getUser(request.getUsername())!=null) {
			log.error("User already exits: {}", request.getUsername());
			throw new UserExistsException("User already exists with this id");
		}
		if(!request.getPsw().equals(request.getConfirmPsw())) {
			log.error("Password & Confirm password should be same");
			throw new PasswordNotMatchedException("Password & Confirm password should be same");
		}

		Login login= Login.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPsw()))
				.registeredAt(LocalDateTime.now())
				.accNonExpired(true)
				.accNonLocked(true)
				.credentialsNonExpired(true)
				.enabled(true)
				.build();
		login.getRoles().add(roleRepository.findByRolename("ROLE_USER"));
		Login res= loginRepository.save(login);
		if(res==null)
			return null;
		
		int aId= areaRepository.findByAreaNameAndPincode(
				request.getArea().getAreaName(), request.getArea().getPincode());
		
		//Validating the area
		if(aId<1) {
			log.error("Invalid area information");
			throw new AreaNotExistsException("Area not exists for the given area information");
		}
		request.getArea().setAreaId(aId);
				
		User user= new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setLogin(login);
		user.setName(request.getName());
		user.setGender(request.getGender());
		user.setDob(LocalDate.parse(request.getDob()));
		user.setOccupation(request.getOccupation());
		user.setPhone(request.getPhone());
		user.setArea(request.getArea());
		log.info("User has been registered successfully");
		return userRepository.save(user);
	}
	
	@Override
	public User getUser(String username) {
		return userRepository.findByLoginUsername(username);
	}

	@Override
	public boolean authentication(AuthenticationRequest request) {
		Authentication auth;
//		try {
//		auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//						request.getUsername(), request.getPassword()));
//		}catch (BadCredentialsException e) {
//			log.error("Incorrect Password");
//			return "BadCredentialsException";
//		}
//		catch (LockedException aee) {
//			log.error("User account is locked");
//			return "LockedException";
//		}
//		catch (DisabledException ale) {
//			log.error("User account is disabled");
//			return "DisabledException";
//		}
		UserDetails userDetails= new UserDetailsServiceImpl().loadUserByUsername(request.getUsername());
		if(!passwordEncoder.matches(request.getPassword(),userDetails.getPassword()))
			return false;
//		if(auth!=null) {
//			Collection<? extends GrantedAuthority> roles= auth.getAuthorities();
//			for(GrantedAuthority role: roles)
//				System.out.println(role.getAuthority());
//		}
//		return jwtUtil.generateToken(loadUserByUsername(request.getUsername()));
		return true;
	}

	@Override
	public boolean isExists(String username) {
		return loginRepository.existsById(username);
	}

	@Transactional
	public boolean resetPsw(String username, ForgotPswRequest request) {
		if(!(request.getNewPsw().equals(request.getCnfPsw()) && request.getNewPsw().length()>5))
			return false;
		Login login= loginRepository.findById(username).get();
		login.setPassword(request.getNewPsw());
		Login newLogin= loginRepository.save(login);
		if(newLogin==null)
			return false;
		return true;
	}

	public void generateFeed() {
		List<User> users= userRepository.findAll();
		List<Crime> crimes= crimeRepository.findAll();
		String[] dts= {
				"2022-02-21 12:02","2022-01-10 05:52","2022-02-11 08:30","2022-07-28 16:45","2022-12-16 16:48",
				"2022-03-01 18:30","2022-05-12 17:54","2022-07-16 12:02","2022-10-29 12:02","2022-11-13 05:45",
				"2022-04-20 22:22","2022-06-09 01:01","2022-08-06 12:02","2022-09-21 12:02","2022-12-24 11:02",
				
				"2021-01-21 12:02","2021-04-19 12:02","2021-07-18 13:02","2021-10-14 11:02","2021-01-31 12:02",
				"2021-02-22 02:02","2021-05-03 11:02","2021-08-04 09:02","2021-11-13 06:02","2021-05-28 12:02",
				"2021-03-12 10:02","2021-06-04 16:02","2021-09-16 08:02","2021-12-12 12:02","2021-06-27 12:02",
				
				"2020-01-01 12:02","2020-04-15 16:22","2022-07-16 13:32","2022-12-02 14:12","2022-01-07 14:12",
				"2020-02-05 15:52","2020-05-01 23:32","2022-09-24 18:52","2022-11-22 18:12","2022-05-25 16:42",
				"2020-03-16 13:42","2020-06-07 22:48","2022-08-07 12:52","2022-10-29 12:42","2022-09-29 22:52",
				
				"2019-01-01 12:02","2019-04-15 16:22","2019-07-16 13:32","2019-12-02 14:12","2019-01-07 14:12",
				"2019-02-05 15:52","2019-05-01 23:32","2019-09-24 18:52","2019-11-22 18:12","2019-05-25 16:42",
				"2019-03-16 13:42","2019-06-07 22:48","2019-08-07 12:52","2019-10-29 12:42","2019-09-29 22:52",
				
				"2018-01-01 12:02","2018-04-15 16:22","2018-07-16 13:32","2018-12-02 14:12","2018-01-07 14:12",
				"2018-02-05 15:52","2018-05-01 23:32","2018-09-24 18:52","2018-11-22 18:12","2018-05-25 16:42",
				"2018-03-16 13:42","2018-06-07 22:48","2018-08-07 12:52","2018-10-29 12:42","2018-09-29 22:52",
				
				"2017-03-16 13:42","2017-06-07 22:48","2017-08-07 12:52","2017-10-29 12:42","2017-09-29 22:52"
				
		};
		List<String> dates= Arrays.asList(dts);
		Random random= new Random();
		for(int i=0; i<10000; i++) {
			Feedback feedback= new Feedback();
			feedback.setFeedId(UUID.randomUUID().toString());
			DateTimeFormatter f= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			feedback.setFeedDateTime(LocalDateTime.parse(dates.get(random.nextInt(dates.size())), f));
			feedback.setUser(users.get(random.nextInt(users.size())));
			feedback.setCrime(crimes.get(random.nextInt(crimes.size())));
			feedback.setActive(true);
			Feedback res= feedbackRepository.save(feedback);
			if(res!=null)
				System.out.println("Feedback No: "+i+" saved");
		}
	}
	
}
