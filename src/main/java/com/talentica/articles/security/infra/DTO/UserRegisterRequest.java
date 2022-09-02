package com.talentica.articles.security.infra.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserRegisterRequest {
	private String username;
	private String password;
	private Integer role;
}
