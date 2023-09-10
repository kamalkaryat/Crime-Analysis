package com.k2dev.ca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.k2dev.ca.controller.LoginSuccessHandler;
import com.k2dev.ca.service.UserDetailsServiceImpl;
import com.k2dev.ca.util.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class CrimeAnalysisConfig extends WebSecurityConfigurerAdapter {
	
//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//Store the actual user-information
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(this.UserDetailServiceImp);
//	}

//	@Bean
//	public AuthenticationSuccessHandler authenticationSuccessHandler() {
//		return new AuthenticationSuccessHandlerImpl();
//	}
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	//Resource access control logic
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		CustomAuthenticationFilter customAuthenticationFilter= new CustomAuthenticationFilter(authenticationManager());
//		customAuthenticationFilter.setFilterProcessesUrl("/auth");
		
		//security purpose
		http.csrf().disable();
		//access control for different-2 URLs
		http
			.authorizeRequests()
			.antMatchers("/user/*").hasRole("USER")
			.antMatchers("/admin/*").hasRole("ADMIN")
			.antMatchers("/anonymous*").anonymous()
			.antMatchers("/static*").permitAll()
			.antMatchers("/**").permitAll()
			.anyRequest()
			.authenticated()
			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
//				.successHandler(loginSuccessHandler)
			.and()	
			.logout()
				.permitAll()
				.logoutUrl("/logout")
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID","remember-me")
				.logoutSuccessUrl("/login");
//			.and()
//			
//			.successHandler(authenticationSuccessHandler());
//	http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//		http.addFilter(new CustomAuthenticationFilter(authenticationManager()));
	}
	
}
