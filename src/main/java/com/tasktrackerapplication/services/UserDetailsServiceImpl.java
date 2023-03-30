package com.tasktrackerapplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tasktrackerapplication.exceptions.UserNotFoundException;
import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
		return userRepository.findUserByEmail(username).orElseThrow(() -> new UserNotFoundException());
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public boolean checkUserAlreadyExist(String username) {
		return userRepository.existsByEmail(username);
	}
}
