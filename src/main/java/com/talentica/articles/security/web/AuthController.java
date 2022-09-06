package com.talentica.articles.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentica.articles.security.AuthService;

import lombok.extern.slf4j.Slf4j;


@RestController
@CrossOrigin
@RequestMapping(AuthController.BASE_URL)
@Slf4j
public class AuthController {
	
	public static final String BASE_URL = "/api/auth";
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value="/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JwtTokenResponse> login(@Validated @RequestBody UserLoginRequest userLoginRequest) throws Exception{
		log.info("Processing login request for user {}",userLoginRequest.getUsername());
		return ResponseEntity.ok(authService.login(userLoginRequest));
	}
	
	@PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserRegisterResponse> registerNewUser(@Validated @RequestBody UserRegisterRequest userRegisterRequest) throws Exception{
		log.info("Processing register request for user {}",userRegisterRequest.getUsername());
		return ResponseEntity.ok(authService.registerNewUser(userRegisterRequest));
	}
	
	@DeleteMapping(value = "/deleteUser/{userId}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId){
		log.info("Processing delete user request for user {}",userId);
		return ResponseEntity.ok(authService.deleteUser(userId));
	}
}
