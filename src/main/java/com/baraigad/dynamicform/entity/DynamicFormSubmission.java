package com.baraigad.dynamicform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dynamic_form_submissions")
public class DynamicFormSubmission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dynamic_form_submission_id") private Long submissionId;
    @Column(name = "dynamic_form_id", nullable = false) private Long formId;
    @Lob @Column(name = "responses_json", nullable = false) private String responsesJson;
    @Column(name = "consent_accepted", nullable = false) private boolean consentAccepted;
    @Column(name = "submitted_at", nullable = false) private LocalDateTime submittedAt;
}
