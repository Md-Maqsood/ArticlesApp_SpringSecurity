package com.talentica.articles.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private  JwtTokenService jwtTokenService;
	
	@Autowired
	private  JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private  CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtTokenService);
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		
		httpSecurity.csrf().disable();
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
		httpSecurity.authorizeRequests()
					.antMatchers("/api/auth/login","/api/auth/register","/api/article/authors").permitAll()
					.antMatchers("/api/article/read/**","/api/article/list").hasAnyAuthority("ROLE_USER","ROLE_AUTHOR","ROLE_ADMIN")
					.antMatchers("/api/article/edit/**","/api/article/create").hasAnyAuthority("ROLE_AUTHOR","ROLE_ADMIN")
					.antMatchers("/api/auth/deleteUser/**","/api/auth/changeRole","/api/article/delete/**").hasAnyAuthority("ROLE_ADMIN")
					.anyRequest().authenticated();
					
		httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
}
