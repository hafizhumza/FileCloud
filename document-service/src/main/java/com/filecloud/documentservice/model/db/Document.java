package com.filecloud.documentservice.model.db;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.filecloud.documentservice.constant.DocumentQueries;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "document")
@NamedQueries({
		@NamedQuery(name = DocumentQueries.QUERY_USER_FILES_SIZE, query = "SELECT COALESCE(SUM(sizeInBytes), 0) FROM Document WHERE userId = :userId")
})
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "document_id")
	private Long documentId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "display_name")
	private String displayName;

	@Lob
	@Column
	private String description;

	@Column
	private String extension;

	@Lob
	@Column
	private String path;

	@Column(name = "size_in_bytes")
	private Long sizeInBytes;

	@Column
	private Boolean recycled;

	@Column(name = "create_date")
	private Long createDate;

	@Column(name = "modify_date")
	private Long modifyDate;

	@OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
	private List<SharedDocument> sharedDocuments;

	@PrePersist
	protected void onCreate() {
		createDate = System.currentTimeMillis();
	}

	@PreUpdate
	protected void onUpdate() {
		modifyDate = System.currentTimeMillis();
	}

}
