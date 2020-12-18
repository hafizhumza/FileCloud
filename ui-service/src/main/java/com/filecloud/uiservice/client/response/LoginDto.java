package com.filecloud.uiservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;

}
