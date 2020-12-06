package com.filecloud.documentservice.repository;

import com.filecloud.documentservice.model.db.SharedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SharedDocumentRepository extends JpaRepository<SharedDocument, Long> {

    Optional<SharedDocument> findByToken(String token);

}
