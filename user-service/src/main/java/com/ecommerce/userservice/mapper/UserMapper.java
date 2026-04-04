package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toEntity(RegisterUserDTO dto)
    {
        User user =  new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setMobileNo(dto.getMobileNo());

        return user;
    }

    public UserResponseDTO toResponse(User user)
    {
        UserResponseDTO response = new UserResponseDTO();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUserName(user.getUserName());
        response.setMobileNo(user.getMobileNo());
        response.setRole(user.getRole());

        return response;
    }
}
