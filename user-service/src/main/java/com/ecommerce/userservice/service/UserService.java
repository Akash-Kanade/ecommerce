package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.exception.UserAlreadyExistException;

public interface UserService {
    UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistException;
}
