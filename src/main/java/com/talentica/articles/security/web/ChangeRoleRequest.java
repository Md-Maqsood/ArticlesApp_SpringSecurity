package com.talentica.articles.security.web;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {

	@NonNull
	private Integer userId;
	
	@NonNull
	private Integer newRoleId;
}
