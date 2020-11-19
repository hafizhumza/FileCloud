package com.filecloud.authserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDto {

    private Long id;
    private String fullName;
    private String email;
    private Boolean accountNonLocked;

}
