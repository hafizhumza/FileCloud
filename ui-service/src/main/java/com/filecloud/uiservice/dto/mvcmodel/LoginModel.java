package com.filecloud.uiservice.dto.mvcmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {

    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be null")
    private String password;

}
