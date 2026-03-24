package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.LoginDTO;
import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.UserAlreadyExistException;
import com.ecommerce.userservice.exception.WrongCredentialException;
import com.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegisterController {
    private final UserService userService;

    RegisterController(UserService service)
    {
        this.userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserDTO dto) throws UserAlreadyExistException {
        UserResponseDTO response = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public  ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) throws WrongCredentialException {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(userService.loginUser(dto));
    }
}

