package com.filecloud.documentservice.repository;

import com.filecloud.documentservice.constant.DocumentQueries;
import com.filecloud.documentservice.model.db.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByUserId(long userId);

    @Query(name = DocumentQueries.QUERY_USER_FILES_SIZE)
    double findUsedSpace(long userId);

}
