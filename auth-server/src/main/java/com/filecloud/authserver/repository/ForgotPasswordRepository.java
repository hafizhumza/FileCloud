package com.filecloud.authserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.filecloud.authserver.model.db.ForgotPassword;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

	Optional<ForgotPassword> findByToken(String token);

	Optional<ForgotPassword> findByUserId(Long userId);

}
