package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.UserAlreadyExistException;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository repository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder encoder;


    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void registerUserShouldRegisterUserSuccessfully() throws UserAlreadyExistException {
        // Arrange
        String encoded_password = "asdfghjertyui";
        RegisterUserDTO dto = getRegisterUserDto();
        User user = setUser(dto);
        User saved = setUser(dto);
        saved.setId(1L);

        UserResponseDTO userResponseDTO = setUserResponseDto(saved);


        Mockito.when(repository.existsUserByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(userMapper.toEntity(dto)).thenReturn(user);
        Mockito.when(encoder.encode(dto.getPassword())).thenReturn(encoded_password);
        Mockito.when(repository.save(user)).thenReturn(saved);
        Mockito.when(userMapper.toResponse(saved)).thenReturn(userResponseDTO);

        // Act

        UserResponseDTO response = userService.registerUser(dto);



        // Assert
        assertNotNull(response);
        assertEquals(dto.getUserName(), response.getUserName());
        assertEquals(dto.getMobileNo(), response.getMobileNo());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(1, response.getId());

        //verify
        Mockito.verify(repository).existsUserByEmail(dto.getEmail());
        Mockito.verify(userMapper).toEntity(dto);
        Mockito.verify(repository).save(user);
        Mockito.verify(encoder).encode(dto.getPassword());
        Mockito.verify(userMapper).toResponse(saved);
    }

    @Test
    public void registerUserShouldThrowException() throws UserAlreadyExistException {
        // Arrange

        RegisterUserDTO dto = getRegisterUserDto();
        User user = setUser(dto);

        Mockito.when(repository.existsUserByEmail(dto.getEmail())).thenReturn(true);

        // Act
        // assert
        Assertions.assertThrows(UserAlreadyExistException.class, ()-> userService.registerUser(dto));

        // verify
        Mockito.verify(repository).existsUserByEmail(dto.getEmail());
        Mockito.verify(repository, Mockito.never()).save(user);
    }

    private UserResponseDTO setUserResponseDto(User saved) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(saved.getId());
        response.setUserName(saved.getUserName());
        response.setEmail(saved.getEmail());
        response.setMobileNo(saved.getMobileNo());
        response.setRole(saved.getRole());
        return response;
    }

    private User setUser(RegisterUserDTO dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setActive(true);
        user.setMobileNo(dto.getMobileNo());
        return user;
    }

    private RegisterUserDTO getRegisterUserDto()
    {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUserName("Rahul Sharma");
        dto.setEmail("rahul.sharma@gmail.com");
        dto.setMobileNo("9876543210");
        dto.setPassword("Rahul123");
        return dto;
    }

}