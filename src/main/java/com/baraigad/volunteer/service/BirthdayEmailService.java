package com.baraigad.volunteer.service;

public interface BirthdayEmailService {

    void sendBirthdayEmails();
    int getTodaysBirthdayCount();
    void sendHtmlEmail(
            String to,
            String subject,
            String htmlContent
    );
}