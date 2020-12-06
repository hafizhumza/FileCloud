package com.filecloud.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.filecloud.authserver.model.db.OAuthAccessToken;

import java.util.Optional;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken, String> {

    Optional<OAuthAccessToken> findByUserName(String username);

    Optional<OAuthAccessToken> findByTokenId(String tokenId);

}
