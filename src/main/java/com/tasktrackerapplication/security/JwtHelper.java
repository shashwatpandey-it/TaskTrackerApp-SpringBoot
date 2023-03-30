package com.tasktrackerapplication.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtHelper {
	
	private static final String SECRET_KEY_STRING = "2646294A404E635266556A586E327235753878214125442A472D4B6150645367";

	// extracting user name from the token
	public String extractUserName(String token) {
		return extractCalim(token, Claims::getSubject);
	}
	
	// validating the JWT token
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpried(token));
	}
	
	// checking if the token is expires or not
	private boolean isTokenExpried(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// extracting the expiration date from the token
	private Date extractExpiration(String token) {
		return extractCalim(token, Claims::getExpiration);
	}
	
	// extracting a specific claim as per the function arguments using the token to first get all claims and the getting specific claim using claim resolver 
	private <T> T extractCalim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	// extracting all the claims from the given JWT token
	private Claims extractAllClaims(String token) {
		return Jwts
					.parserBuilder()
					.setSigningKey(getSignInKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}
	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_STRING);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	// generating token if no extra claims are given
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	// generating token actually with extra claims, even if there are none
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts
					.builder()
					.setClaims(extraClaims)
					.setSubject(userDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 5*60*60*1000))
					.signWith(getSignInKey())
					.compact();
	}
}
