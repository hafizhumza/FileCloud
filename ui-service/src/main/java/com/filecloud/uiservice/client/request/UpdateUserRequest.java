package com.filecloud.uiservice.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    private String name;

    @Email(message = "Invalid Email")
    private String email;

}
