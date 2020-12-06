package com.filecloud.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filecloud.authserver.model.db.Role;
import com.filecloud.authserver.repository.RoleRepository;


@Service
public class RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}
}
