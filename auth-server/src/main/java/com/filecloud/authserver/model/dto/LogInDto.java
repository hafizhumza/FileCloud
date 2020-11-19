
package com.filecloud.authserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogInDto {

    private String clientId;
    private String clientSecret;
    private String email;
    private String password;

}
