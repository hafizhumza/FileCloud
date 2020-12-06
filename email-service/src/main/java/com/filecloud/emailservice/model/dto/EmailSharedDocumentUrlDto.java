package com.filecloud.emailservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSharedDocumentUrlDto {

    private String receiverEmail;

    private String senderName;

    private String url;

    private int expiryDays;

}
