package com.vitalNow.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vitalNow.exception.BadCredentialException;
import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.BadTokenException;
import com.vitalNow.exception.UnauthorizedException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilter extends OncePerRequestFilter {


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader("Authorization");
		if(token !=null) {
			
			try {
				UsernamePasswordAuthenticationToken authentication = TokenUtils.decodeToken(token);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}catch(IllegalArgumentException iae) {
				throw new BadTokenException("Token not present or invalid.");
			}catch(ExpiredJwtException eje) {
				throw new UnauthorizedException("Expired token");
			}catch(MalformedJwtException me) {
				throw new BadTokenException("Not valid token formation.");
			}catch(JwtException je) {
				throw new UnauthorizedException("Invalid token.");
			}catch(Exception e) {
				throw new UnauthorizedException("Authentication error: "+e.getMessage());
			}
			
		}
		filterChain.doFilter(request, response);
		
	}

}
