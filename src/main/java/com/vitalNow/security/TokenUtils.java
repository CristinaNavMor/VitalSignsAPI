package com.vitalNow.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.vitalNow.exception.BadRequestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtils {

	private final static String ACCESS_TOKEN_SECRET="TokenisThisThebestOne?Idon'tThinkSobutwecantry";
	
	//CAMBIAR EN PRODUCCIÓN
	private final static long ACCESS_TOKEN_LIFE_TIME = (long)(10*24*60*60*1000);
	
	
	
	public static String generateToken(String email, String username, String role) {
		Date expirationDate = new Date(System.currentTimeMillis()+ACCESS_TOKEN_LIFE_TIME);
		
		Map<String, Object> payload = new HashMap<>();
		
		payload.put("email", email);
		payload.put("username", username);
		payload.put("role", role);
		
		String token = Jwts.builder()
						.subject(email)
						.expiration(expirationDate)
						.claims(payload)
						.signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
						.compact();
		return "Bearer " + token;
	}
	
	public static UsernamePasswordAuthenticationToken decodeToken (String token) throws JwtException, IllegalArgumentException, MalformedJwtException, ExpiredJwtException {
		if(!token.startsWith("Bearer ")) {
			throw new MalformedJwtException("Formato no encontrado");
		}
		
		token = token.substring(7);
		Claims claims = Jwts.parser()
					.verifyWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
		String email = claims.getSubject();
		String username = (String) claims.get("username");
		String role = (String) claims.get("role");
			
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
		UsernamePasswordAuthenticationToken auth = 	new UsernamePasswordAuthenticationToken(email, null, authorities);
		auth.setDetails(username);
		return auth;

	}

}
