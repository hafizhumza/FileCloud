package com.filecloud.authserver.service;

import com.filecloud.authserver.model.db.OAuthAccessToken;
import com.filecloud.authserver.repository.OAuthAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuthAccessTokenService {

    private final OAuthRefreshTokenService oAuthRefreshTokenService;

    private final OAuthAccessTokenRepository oAuthAccessTokenRepository;

    @Autowired
    public OAuthAccessTokenService(OAuthAccessTokenRepository oAuthAccessTokenRepository, OAuthRefreshTokenService oAuthRefreshTokenService) {
        this.oAuthAccessTokenRepository = oAuthAccessTokenRepository;
        this.oAuthRefreshTokenService = oAuthRefreshTokenService;
    }

    public void deleteUserAccessAndRefreshToken(String username) {
        Optional<OAuthAccessToken> token = oAuthAccessTokenRepository.findByUserName(username);
        token.ifPresent(oAuthAccessToken -> {
            oAuthRefreshTokenService.delete(oAuthAccessToken.getRefreshToken());
            oAuthAccessTokenRepository.delete(oAuthAccessToken);
        });
    }
}
