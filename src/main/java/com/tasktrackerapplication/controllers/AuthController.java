package com.tasktrackerapplication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.requestandresponse.AuthenticationRequest;
import com.tasktrackerapplication.requestandresponse.AuthenticationResponse;
import com.tasktrackerapplication.requestandresponse.RegisterRequest;
import com.tasktrackerapplication.services.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/task-tracker/auth")
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final static int COOKIE_VALIDITY = 5*60*60*1000;
	
	@GetMapping("/entry/regi")
	public String registerPage() {
		logger.info("registration page");
		return "register";
	}
	
	@GetMapping("/entry/login")
	public String loginPage() {
		logger.info("login page");
		return "login";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute RegisterRequest request, BindingResult result, HttpServletResponse response){
		AuthenticationResponse authenticationResponse = authenticationService.register(request);
		logger.info("{}",authenticationResponse.isTokenGenerated());
		if(!authenticationResponse.isTokenGenerated()) {
			logger.info("user already exists");
			return "redirect:/task-tracker/auth/entry/regi";
		}
		Cookie cookie = new Cookie("jwt", authenticationResponse.getToken());
		cookie.setPath("/");
		cookie.setMaxAge(COOKIE_VALIDITY);
		response.addCookie(cookie);
		return "redirect:/task-tracker/index";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute AuthenticationRequest request, BindingResult result, HttpServletResponse response){
		AuthenticationResponse authenticationResponse = authenticationService.login(request);
		Cookie cookie = new Cookie("jwt", authenticationResponse.getToken());
		cookie.setPath("/");
		cookie.setMaxAge(COOKIE_VALIDITY);
		response.addCookie(cookie);
		return "redirect:/task-tracker/index";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "redirect:/task-tracker/auth/entry/login";
	}
}
