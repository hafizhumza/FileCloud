package com.filecloud.emailservice.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_email")
    private String receiverEmail;

    @Lob
    @Column
    private String description;

    @Column(name = "was_sent")
    private boolean wasSent;

    @Column(name = "create_date")
    private Long createDate;

    @PrePersist
    protected void onCreate() {
        createDate = System.currentTimeMillis();
    }

}
