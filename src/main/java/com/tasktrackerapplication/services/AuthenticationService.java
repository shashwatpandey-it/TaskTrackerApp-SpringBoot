package com.tasktrackerapplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.requestandresponse.AuthenticationRequest;
import com.tasktrackerapplication.requestandresponse.AuthenticationResponse;
import com.tasktrackerapplication.requestandresponse.RegisterRequest;
import com.tasktrackerapplication.security.JwtHelper;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		if(userDetailsServiceImpl.checkUserAlreadyExist(request.getEmail())) {
			return AuthenticationResponse.builder()
											.tokenGenerated(false)
											.token(null)
											.build();
		}
		var user = User.builder()
						.firstnameString(request.getFirstName())
						.lastnameString(request.getLastName())
						.email(request.getEmail())
						.passwordString(passwordEncoder.encode(request.getPassword()))
						.build();
		
		userDetailsServiceImpl.save(user);
		
		var jwtToken = jwtHelper.generateToken(user);
		return AuthenticationResponse.builder()
										.tokenGenerated(true)
										.token(jwtToken)
										.userId(user.getId())
										.build();
	}
	
	public AuthenticationResponse login(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		var user = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
		var jwtToken = jwtHelper.generateToken(user);
		System.out.println(jwtToken);
		return AuthenticationResponse.builder()
										.tokenGenerated(true)
										.token(jwtToken)
										.userId(((User) user).getId())
										.build();
	}
}
