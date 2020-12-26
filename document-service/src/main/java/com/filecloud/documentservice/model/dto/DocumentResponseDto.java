package com.filecloud.documentservice.model.dto;

import com.filecloud.documentservice.model.db.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {

	private Long documentId;
	private String displayName;
	private String description;
	private String extension;
	private Long sizeInBytes;
	private Long createDate;
	private Long modifyDate;

	public DocumentResponseDto(Document document) {
		documentId = document.getDocumentId();
		displayName = document.getDisplayName();
		description = document.getDescription();
		extension = document.getExtension();
		sizeInBytes = document.getSizeInBytes();
		createDate = document.getCreateDate();
		modifyDate = document.getModifyDate();
	}

}
