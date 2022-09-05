package com.talentica.articles.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talentica.articles.security.infra.Role;
import com.talentica.articles.security.infra.RoleRepository;
import com.talentica.articles.security.infra.User;
import com.talentica.articles.security.infra.UserRepository;
import com.talentica.articles.security.web.UserRegisterRequest;
import com.talentica.articles.security.web.UserRegisterResponse;

import lombok.RequiredArgsConstructor;

@Service
public class JwtUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserRegisterResponse save(UserRegisterRequest userRegisterRequest) {
		User user = new User();
		user.setUsername(userRegisterRequest.getUsername());
		user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
		user.setUsername(userRegisterRequest.getUsername());
		user.setRole(roleRepository.findById(userRegisterRequest.getRole()).orElse(roleRepository.findById(1).orElse(new Role(1,"ROLE_USER"))));
		User savedUser = userRepository.save(user);
		return new UserRegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole().getName());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
								.orElseThrow(()-> new UsernameNotFoundException("Could not find the username"));
		
		return new CustomUserDetails(user);
	}
	
}
