package com.filecloud.uiservice.dto.response;

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
    private String userRole;
    private Boolean accountNonLocked;

}
