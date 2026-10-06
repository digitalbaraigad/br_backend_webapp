package com.baraigad.dynamicform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dynamic_forms")
public class DynamicForm {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dynamic_form_id") private Long formId;
    @Column(name = "public_id", nullable = false, unique = true, length = 64) private String publicId;
    @Column(nullable = false) private String title;
    @Column(name = "header_image_url", length = 2048) private String headerImageUrl;
    @Column(length = 2000) private String description;
    @Column(name = "consent_text", nullable = false, length = 2000) private String consentText;
    @Column(nullable = false, length = 20) private String status;
    @Column(name = "expiry_date") private LocalDate expiryDate;
    @Lob @Column(name = "fields_json", nullable = false) private String fieldsJson;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
}
