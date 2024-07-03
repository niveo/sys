package br.com.ams.sys.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	String extractUsername(String jwt);

	boolean isTokenValid(String jwt, UserDetails userDetails);
	
	String generateToken(UserDetails userDetails);
}
