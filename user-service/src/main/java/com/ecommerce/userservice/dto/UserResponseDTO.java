package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    @NotBlank
    private long id;

    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String role;
}
