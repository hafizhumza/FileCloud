package com.filecloud.uiservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceInfoResponse {

    private long spaceLimit;
    private long usedSpace;
    private long remainingSpace;

}
