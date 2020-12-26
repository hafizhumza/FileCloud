package com.filecloud.uiservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceInfoResponse {

    private double spaceLimit;
    private double usedSpace;
    private double remainingSpace;

}
