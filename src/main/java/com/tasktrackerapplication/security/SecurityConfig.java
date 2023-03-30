package com.tasktrackerapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
							.csrf()
							.disable()
							.authorizeHttpRequests()
							.requestMatchers("/task-tracker/auth/**", "/webjars/**")
							.permitAll()
							.anyRequest()
							//.permitAll()
							.authenticated()
							.and()
							.exceptionHandling()
							.authenticationEntryPoint(authenticationEntryPoint)
							.and()
							.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and()
							.authenticationProvider(authenticationProvider)
							.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
							.build();
	}
	
}
