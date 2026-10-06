package com.baraigad.enquiry.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contact_enquiries")
public class ContactEnquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enquiry_id")
    private Long enquiryId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email_id", nullable = false, length = 180)
    private String emailId;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "whatsapp_phone_number", nullable = false, length = 15)
    private String whatsappPhoneNumber;

    @Column(name = "message", nullable = false, length = 700)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void setCreatedAt() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
