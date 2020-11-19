package com.filecloud.authserver.service;

import com.filecloud.authserver.model.db.OAuthRefreshToken;
import com.filecloud.authserver.repository.OAuthRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuthRefreshTokenService {

    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

    @Autowired
    public OAuthRefreshTokenService(OAuthRefreshTokenRepository oAuthRefreshTokenRepository) {
        this.oAuthRefreshTokenRepository = oAuthRefreshTokenRepository;
    }

    public void delete(String refreshTokenId) {
        Optional<OAuthRefreshToken> token = oAuthRefreshTokenRepository.findByTokenId(refreshTokenId);
        token.ifPresent(oAuthRefreshTokenRepository::delete);
    }

}
