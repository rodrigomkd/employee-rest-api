package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserNotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@Service
public class UserService {
	
	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(UserService.class);
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.id}")
	private String jwtId;
	
	@Value("${spring.security.user.name}")
	private String username;
	
	@Value("${spring.security.user.password}")
	private String password;
	
	/**
	 * Returns a valid Token.
	 * 
	 * @param username
	 * @param password
	 * @return String token
	 */
	public String getJWTToken(String username, String password) {
		logger.info("Entering getJWTToken()");
		
		authentication(username, password);
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId(jwtId)
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		logger.info("Exiting getJWTToken()");
		
		return "Bearer " + token;
	}
	
	/**
	 * Valid the username and password.
	 * 
	 * @param username
	 * @param password
	 * @return String token
	 */
	private void authentication(String username, String password) {
		logger.info("Entering getJWTToken()");
		
		if(this.username.equals(username) && this.password.equals(password)) {
			logger.info("The username and password are valid.");
		} else {
			throw new UserNotFoundException("The username or Password is incorrect.");
		}
		
		logger.info("Exiting getJWTToken()");
	}
}