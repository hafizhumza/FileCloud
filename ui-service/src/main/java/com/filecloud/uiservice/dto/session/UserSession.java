package com.filecloud.uiservice.dto.session;

import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserSession implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String role;
    private Boolean accountNonLocked;
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;
    private String scope;

    public void setData(LoginResponse response, UserDto dto) {
        accessToken = response.getAccess_token();
        expiresIn = response.getExpires_in();
        refreshToken = response.getRefresh_token();
        scope = response.getScope();
        tokenType = response.getToken_type();
        email = dto.getEmail();
        role = dto.getUserRole();
        name = dto.getFullName().toUpperCase();
        accountNonLocked = dto.getAccountNonLocked();
        id = dto.getId();
    }

}
