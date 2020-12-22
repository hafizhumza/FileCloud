package com.filecloud.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filecloud.authserver.exception.InvalidAccessException;
import com.filecloud.authserver.model.db.ForgotPassword;
import com.filecloud.authserver.repository.ForgotPasswordRepository;


@Service
public class ForgotPasswordService {

	private final ForgotPasswordRepository forgotPasswordRepository;

	@Autowired
	public ForgotPasswordService(ForgotPasswordRepository forgotPasswordRepository) {
		this.forgotPasswordRepository = forgotPasswordRepository;
	}

	public ForgotPassword findByToken(String token) {
		return forgotPasswordRepository.findByToken(token).orElseThrow(InvalidAccessException::new);
	}

	public void save(ForgotPassword forgotPassword) {
		forgotPasswordRepository.save(forgotPassword);
	}
}
