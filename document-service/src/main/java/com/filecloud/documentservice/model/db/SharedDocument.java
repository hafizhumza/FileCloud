package com.filecloud.documentservice.model.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
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
