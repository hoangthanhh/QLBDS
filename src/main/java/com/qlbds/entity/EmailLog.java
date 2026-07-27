package com.qlbds.entity;

import com.qlbds.constant.EmailStatusEnum;
import com.qlbds.constant.EmailTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_log")
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "recipient_email", length = 100, nullable = false)
    private String recipientEmail;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailTypeEnum emailType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmailStatusEnum status;

    @Column(name = "sent_at", insertable = false, updatable = false)
    private LocalDateTime sentAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public EmailTypeEnum getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailTypeEnum emailType) {
        this.emailType = emailType;
    }

    public EmailStatusEnum getStatus() {
        return status;
    }

    public void setStatus(EmailStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}