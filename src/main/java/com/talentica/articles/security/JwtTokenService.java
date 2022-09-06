package com.talentica.articles.security;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.talentica.articles.exception.JwtTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

	private static final long ACCESS_TOKEN_EXPIRE_TIME = 24*60*60*1000;
		
	public static final String ROLES = "ROLES";
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	public static final String BEARER_PREFIX = "Bearer";
	
	@Value("${security.jwt.token.secret:secret}")
	private String secret;
	
	private final JwtUserDetailsService jwtUserDetailsService;
	
	private Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
			throw new JwtTokenException("Invalid Access Token");
		}
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(getAllClaimsFromToken(token));
	}
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public List<String> getRolesFromToken(String token){
		return getClaimFromToken(token, claims -> (List) claims.get(ROLES));
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	private Boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).before(new Date());
	}
	
	public String createToken(Authentication authentication) {
		final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Claims claims = Jwts.claims().setSubject(customUserDetails.getUsername());
		final List<String> roles = customUserDetails.getAuthorities()
									.stream().map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList());
		claims.put(ROLES, roles);
		
		Date now = new Date();
		
		String accessToken = Jwts.builder()
							.setClaims(claims)
							.setIssuedAt(now)
							.setExpiration(new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME))
							.signWith(SignatureAlgorithm.HS512, secret)
							.compact();
		
		return accessToken;
	}
	
	public Authentication getAuthenticationFromToken(String token) throws UsernameNotFoundException {
		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(getUsernameFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if(bearerToken != null && bearerToken.startsWith(BEARER_PREFIX+" ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	public Boolean validateToken(String token) {
		final String username = getUsernameFromToken(token);
		return username != null && !isTokenExpired(token);
	}
}
