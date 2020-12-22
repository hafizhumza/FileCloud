package com.filecloud.authserver.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "auth_user")
@Data
public class ForgotPassword implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Long userId;

	@Column
	private Boolean availed;

	@Lob
	@Column
	private String token;

	@Column
	private Long createDate;

	public ForgotPassword() {
	}

	@PrePersist
	protected void onCreate() {
		createDate = System.currentTimeMillis();
	}

}
