package com.filecloud.documentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceInfoResponseDto {

	private long spaceLimit;
	private long usedSpace;
	private long remainingSpace;

}
