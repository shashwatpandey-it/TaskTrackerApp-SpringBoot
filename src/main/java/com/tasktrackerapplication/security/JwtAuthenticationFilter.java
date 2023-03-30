package com.tasktrackerapplication.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	private final static String JWT_COOKIE_NAME_STRING = "jwt";
	
	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// check for and extract if exist the header in the request
		//final String authHeader = request.getHeader("Authorization");    --to be used with request headers
		final String jwtToken = getTokenFromCookie(request);
		//logger.info("authentication header : {}", authHeader);
		
		if(jwtToken == null) {
			logger.info("either no cookie found or no token inside the cookie...");
			filterChain.doFilter(request, response);
			return;
		}
		
		
		final String userName = jwtHelper.extractUserName(jwtToken);
		
		if(userName != null && (SecurityContextHolder.getContext().getAuthentication() == null)) {
			// since user name is not null we try to validate the user 
			User user = (User) userDetailsServiceImpl.loadUserByUsername(userName);
			if(jwtHelper.isTokenValid(jwtToken, user)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
							new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); 
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				request.setAttribute("user", user);
			}
			
		}
		// if user is not validated due to any reason then we invoke next filter in the chain
		filterChain.doFilter(request, response);
	}
	
	private String getTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(JWT_COOKIE_NAME_STRING.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

}
