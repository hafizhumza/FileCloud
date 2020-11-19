package com.filecloud.authserver.repository;

import com.filecloud.authserver.model.db.OAuthRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRefreshTokenRepository extends JpaRepository<OAuthRefreshToken, String> {

    Optional<OAuthRefreshToken> findByTokenId(String tokenId);

}
