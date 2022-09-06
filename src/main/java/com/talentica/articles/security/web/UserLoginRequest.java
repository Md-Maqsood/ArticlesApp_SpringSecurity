package com.talentica.articles.security.web;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;
}
