package com.talentica.articles.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentica.articles.security.JwtTokenService;
import com.talentica.articles.security.JwtUserDetailsService;


@RestController
@CrossOrigin
@RequestMapping(AuthController.BASE_URL)
public class AuthController {
	
	public static final String BASE_URL = "/api/auth";
	
	@Autowired
	private AuthenticationManager authenticationManger;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@PostMapping(value="/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JwtTokenResponse> authenticateAndCreateToken(@Validated @RequestBody UserLoginRequest userLoginRequest) throws Exception{
		final Authentication authentication = authenticationManger.authenticate(
														new UsernamePasswordAuthenticationToken(
																userLoginRequest.getUsername(),
																userLoginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return ResponseEntity.ok(new JwtTokenResponse(jwtTokenService.createToken(authentication)));
	}
	
	@PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserRegisterResponse> registerNewUser(@Validated @RequestBody UserRegisterRequest userRegisterRequest) throws Exception{
		return ResponseEntity.ok(jwtUserDetailsService.save(userRegisterRequest));
	}
}
