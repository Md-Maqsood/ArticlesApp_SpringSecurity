package com.talentica.articles.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.talentica.articles.exception.InvalidRequestException;
import com.talentica.articles.exception.UserNotFoundException;
import com.talentica.articles.security.infra.Role;
import com.talentica.articles.security.infra.RoleRepository;
import com.talentica.articles.security.infra.User;
import com.talentica.articles.security.infra.UserRepository;
import com.talentica.articles.security.web.ChangeRoleRequest;
import com.talentica.articles.security.web.JwtTokenResponse;
import com.talentica.articles.security.web.UserLoginRequest;
import com.talentica.articles.security.web.UserRegisterRequest;
import com.talentica.articles.security.web.UserRegisterResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	@Autowired
	private JwtTokenService jwtTokenService;
		
	@Autowired
	private AuthenticationManager authenticationManger;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	

	public JwtTokenResponse login(UserLoginRequest userLoginRequest) {
		final Authentication authentication = authenticationManger.authenticate(
				new UsernamePasswordAuthenticationToken(
						userLoginRequest.getUsername(),
						userLoginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info("Logging in user {}",userLoginRequest.getUsername());
		return new JwtTokenResponse(jwtTokenService.createToken(authentication));
	}

	public UserRegisterResponse registerNewUser(UserRegisterRequest userRegisterRequest) {
		log.info("Crating new user {}",userRegisterRequest.getUsername());
		return jwtUserDetailsService.save(userRegisterRequest);		
	}

	public Boolean deleteUser(Integer userId) {
		User user = userRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException(String.format("User with user id %s does not exist", userId))
				);
		userRepository.delete(user);
		log.info("Deleted user {}",userId);
		return true;				
	}

	public Boolean changeRoleOfUser(ChangeRoleRequest changeRoleRequest) {
		User user = userRepository.findById(changeRoleRequest.getUserId()).orElseThrow(
				() -> new UserNotFoundException(String.format("User with user id %s does not exist", changeRoleRequest.getUserId()))
				);
		Role newRole = roleRepository.findById(changeRoleRequest.getNewRoleId()).orElseThrow(
				() -> new InvalidRequestException("The new role not found")
				);
		user.setRole(newRole);
		userRepository.save(user);
		log.info("Role of user {} changed to {}",user.getUsername(), user.getRole().getName());
		return true;
	}
}
