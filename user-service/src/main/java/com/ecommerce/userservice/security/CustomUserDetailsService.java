package com.ecommerce.userservice.security;

import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = "user")
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
      log.info("Fetching credentials from the Database for: ${}",email);
      User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User with "+ email +" not found"));

        return new CustomUserDetails(user);
    }
}
