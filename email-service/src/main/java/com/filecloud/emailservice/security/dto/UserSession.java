package com.filecloud.emailservice.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {

    private static final long serialVersionUID = 5218700171184348491L;

    private Long userId;
    private String fullName;
    private String email;

}
