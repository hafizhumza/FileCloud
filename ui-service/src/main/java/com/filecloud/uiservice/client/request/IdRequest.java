package com.filecloud.uiservice.client.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdRequest {

    @NotNull(message = "ID cannot be null")
    @Min(value = 1, message = "Invalid ID")
    private Long id;

}
