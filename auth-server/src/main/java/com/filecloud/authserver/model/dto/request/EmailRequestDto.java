
package com.filecloud.authserver.model.dto.request;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDto {

	@Email(message = "Invalid email address")
	private String email;

}
