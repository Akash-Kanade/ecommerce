package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.LoginDTO;
import com.ecommerce.userservice.utility.JwtUtility;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final CacheManager cacheManager;

    public AuthService(JwtUtility jwtUtility, AuthenticationManager authenticationManager, CacheManager cacheManager)
    {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.cacheManager = cacheManager;
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

    public String logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtility.extractUsername(token);
        Cache userCache = cacheManager.getCache("user");
        if (userCache != null) {
            userCache.evict(email);
        }

        return email;
    }
}
