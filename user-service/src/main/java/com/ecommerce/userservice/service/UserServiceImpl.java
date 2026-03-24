package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.UserAlreadyExistException;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository repo, UserMapper userMapper, PasswordEncoder encoder)
    {
        this.repository = repo;
        this.userMapper = userMapper;
        this.passwordEncoder = encoder;

    }

    @Override
    public UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistException {
        if(repository.existsUserByEmail(registerUserDTO.getEmail()))
            throw new UserAlreadyExistException("User with email: " + registerUserDTO.getEmail() + " already exist");

        User user = userMapper.toEntity(registerUserDTO);
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));

        // defaults
        user.setActive(true);
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        // save
        User savedUser = repository.save(user);

        return userMapper.toResponse(savedUser);
    }
}
