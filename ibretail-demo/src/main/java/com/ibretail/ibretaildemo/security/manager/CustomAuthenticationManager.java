package com.ibretail.ibretaildemo.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.ibretail.ibretaildemo.membership.User;
import com.ibretail.ibretaildemo.membership.UserService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager {
	private UserService userServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userServiceImpl.findByEmail(authentication.getName());
        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect password.");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }
	
	
}
