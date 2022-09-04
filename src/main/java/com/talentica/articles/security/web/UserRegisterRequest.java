package com.talentica.articles.security.web;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class UserRegisterRequest {
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;
	
	@NonNull
	private Integer role;
}
