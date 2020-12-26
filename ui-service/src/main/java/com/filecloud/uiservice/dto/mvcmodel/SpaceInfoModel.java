package com.filecloud.uiservice.dto.mvcmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SpaceInfoModel {

    private String spaceLimit;
    private String usedSpace;
    private String remainingSpace;

    public SpaceInfoModel() {
        spaceLimit = "0 B";
        usedSpace = "0 B";
        remainingSpace = "0 B";
    }

}
