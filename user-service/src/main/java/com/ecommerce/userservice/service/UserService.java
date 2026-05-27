package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserRequestDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.UserAlreadyExistException;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistException;
    List<UserResponseDTO> getAllUser();
    UserResponseDTO getUserById(long id);
    UserResponseDTO updateUser(UserRequestDTO userDTO);
    UserResponseDTO updateUserProperty(long id, String property, String value);

    String deleteUserById(long id);
}
