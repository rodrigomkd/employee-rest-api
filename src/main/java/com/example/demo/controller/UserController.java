package com.example.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@RestController
@RequestMapping(UserController.USER_API)
public class UserController {

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	public static final String USER_API = "/api/v1/auth";
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * Returns a User with a valid token.
	 * 
	 * @param user
	 * @param password
	 * @return ResponseEntity<Object> User object
	 */
	@ApiOperation(value = "Retrieve User with token Authorization", notes =
			"Provide user and password")
	@PostMapping("/token")
	public ResponseEntity<Object> login(@RequestParam("user") String username, 
			@RequestParam("password") String pwd) {
		logger.info("[POST] Retrieving user token.");
		
		String token = userService.getJWTToken(username, pwd);
		User user = new User();
		user.setUser(username);
		user.setToken(token);
		
		logger.info("[POST] User found.");
		return ResponseEntity.ok(user);
	}
}
