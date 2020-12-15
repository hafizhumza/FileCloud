package com.filecloud.uiservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;

}
