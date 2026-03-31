package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.LoginDTO;
import com.ecommerce.userservice.utility.JwtUtility;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtUtility jwtUtility, AuthenticationManager authenticationManager)
    {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
    }

    public String login(LoginDTO dto)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userDetails != null)
            return jwtUtility.generateToken(userDetails.getUsername());
        else
            throw new RuntimeException("Something went wrong...");
    }
}
