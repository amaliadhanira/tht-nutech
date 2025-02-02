package com.ibretail.ibretaildemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.ibretail.ibretaildemo.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
	
	private final CustomAuthenticationManager customAuthenticationManager = new CustomAuthenticationManager();

	 	@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 		http        
	 			.headers().frameOptions().disable() // New Line: the h2 console runs on a "frame". By default, Spring Security prevents rendering within an iframe. This line disables its prevention.
	 			.and()
	 			.csrf().disable()
	 			.authorizeRequests() 
				 .anyRequest().permitAll()
				 .and()
	 			 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 		
	 		return http.build();
	 	}
}