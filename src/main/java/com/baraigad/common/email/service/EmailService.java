package com.baraigad.common.email.service;

public interface EmailService {
  void sendHtmlEmail(
                String to,
                String subject,
                String htmlContent);

}