package com.talentica.articles.security.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {
	private Integer id;
	private String username;
	private String role;
}
