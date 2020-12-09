package com.filecloud.documentservice.model.db;

import com.filecloud.documentservice.constant.DocumentQueries;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "document")
@NamedQueries({
        @NamedQuery(name = DocumentQueries.QUERY_USER_FILES_SIZE, query = "SELECT COALESCE(SUM(sizeInMb), 0) FROM Document WHERE userId = :userId")
})
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "display_name")
    private String displayName;

    @Lob
    @Column
    private String description;

    @Column
    private String extension;

    @Lob
    @Column
    private String path;

    @Column(name = "size_in_mb")
    private Double sizeInMb;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "modify_date")
    private Long modifyDate;

    @OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
    private List<SharedDocument> sharedDocuments;

    @PrePersist
    protected void onCreate() {
        createDate = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = System.currentTimeMillis();
    }

}
