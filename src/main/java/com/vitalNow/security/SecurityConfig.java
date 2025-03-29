package com.vitalNow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.vitalNow.services.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	RequestFilter requestFilter;
	
	@Bean
	UserService userDetailsService() {
		return new UserService();
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean 
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(this.userDetailsService());
		authProvider.setPasswordEncoder(this.passwordEncoder());
		return authProvider;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((request)->{
			request.requestMatchers("/swagger-ui/**").permitAll()
					.requestMatchers("/v3/api-docs/**").permitAll()
					.requestMatchers("/login").permitAll()
					.requestMatchers("/register").hasAuthority("ADMIN")
					.requestMatchers("/users").authenticated()
					.requestMatchers("/user/admin/**").hasAuthority("ADMIN")
					.requestMatchers("/user/**").authenticated()
					.requestMatchers("/patients").authenticated()
					.requestMatchers(HttpMethod.GET, "/patient/**").authenticated()
					.requestMatchers("/patient/register").hasAuthority("DOCTOR")
					.requestMatchers("/patient/{medicalRecordNumber}/records").authenticated()
					.requestMatchers("/patient/{medicalRecordNumber}/record/**").hasAnyAuthority("NURSE", "DOCTOR")
					.requestMatchers("/patient/**").hasAuthority("DOCTOR")
					.anyRequest().denyAll();
		});
		http.csrf(csrf-> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	

}
