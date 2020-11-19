package com.filecloud.authserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filecloud.authserver.model.db.AuthUser;


public interface UserRepository extends JpaRepository<AuthUser, Long> {

	Optional<AuthUser> findByEmail(String email);

	List<AuthUser> findByAccountNonLocked(boolean accountNonLocked);

}
