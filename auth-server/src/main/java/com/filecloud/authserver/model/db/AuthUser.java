package com.filecloud.authserver.model.db;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "auth_user")
@Data
public class AuthUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "full_name")
	private String fullName;

	@Column
	private String email;

	@Column
	private String password;

	@Column
	private boolean enabled;

	@Column
	private boolean accountNonExpired;

	@Column
	private boolean credentialsNonExpired;

	@Column
	private boolean accountNonLocked;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_user", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id") })
	private List<Role> roles;

	public AuthUser() {
	}

	public AuthUser(AuthUser authUser) {
		this.fullName = authUser.getFullName();
		this.email = authUser.getEmail();
		this.password = authUser.getPassword();
		this.enabled = authUser.isEnabled();
		this.accountNonExpired = authUser.isAccountNonExpired();
		this.credentialsNonExpired = authUser.isCredentialsNonExpired();
		this.accountNonLocked = authUser.isAccountNonLocked();
		this.roles = authUser.getRoles();
	}
}
