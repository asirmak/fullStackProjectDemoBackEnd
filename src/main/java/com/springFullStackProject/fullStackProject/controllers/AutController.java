package com.springFullStackProject.fullStackProject.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springFullStackProject.fullStackProject.entities.User;
import com.springFullStackProject.fullStackProject.requests.UserRequest;
import com.springFullStackProject.fullStackProject.security.JwtTokenProvider;
import com.springFullStackProject.fullStackProject.services.UserServices;

@RestController
@RequestMapping("/auth")
public class AutController {

	private AuthenticationManager authenticationManager;
	
	private JwtTokenProvider jwtTokenProvider;
	
	private UserServices userServices;
	
	private PasswordEncoder passwordEncoder;
	
	public AutController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			UserServices userServices, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userServices = userServices;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public String login(@RequestBody UserRequest loginRequest) {
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
	        Authentication auth = authenticationManager.authenticate(authToken);
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
	        return jwtToken;
	}

	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRequest registerRequest){
		if(userServices.getOneUserByUserName(registerRequest.getUsername()) != null) {
			return new ResponseEntity<>("Username already in use.", HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setUserName(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userServices.createUser(user);
		return new ResponseEntity<>("User successfully registered.", HttpStatus.CREATED);
	}
	
}