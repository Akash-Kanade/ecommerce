package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.service.AuthService;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.utility.JwtUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService service;

    //  This bean is required for the spring context and for jwtFilter at the time of spring context loading

    @MockitoBean
    JwtUtility jwtUtility;

    @MockitoBean
    private AuthService authService;

    @Test
    public void shouldRegisterWhenCalled() throws Exception {
        //Arrange
        RegisterUserDTO request = new RegisterUserDTO();
        request.setPassword("Akash123");
        request.setEmail("akash123@gmail.com");
        request.setMobileNo("8390968203");
        request.setUserName("Akash Kanade");

        UserResponseDTO response = new UserResponseDTO();
        response.setRole("USER");
        response.setId(1);
        response.setUserName(request.getUserName());
        response.setEmail(request.getEmail());
        response.setMobileNo(request.getMobileNo());

        //  when(userService.registerUser(request)).thenReturn(response);
        when(service.registerUser(any())).thenReturn(response);

        // Act
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.userName").value(response.getUserName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.mobileNo").value(response.getMobileNo()));

        // Verify
        verify(service).registerUser(any());


    }


}
