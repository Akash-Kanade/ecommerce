package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String mobileNo;

}
