package com.filecloud.documentservice.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "shared_documents")
public class SharedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Lob
    @Column
    private String token;

    @Column(name = "create_date")
    private Long createDate;

    @PrePersist
    protected void onCreate() {
        createDate = System.currentTimeMillis();
    }

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

}
