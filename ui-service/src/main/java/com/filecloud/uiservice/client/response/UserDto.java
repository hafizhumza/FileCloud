package com.filecloud.uiservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "ID null")
    @Min(value = 1, message = "Invalid ID")
    private Long id;

    private String fullName;

    private String email;

    private String userRole;

    private Boolean accountNonLocked;

}
