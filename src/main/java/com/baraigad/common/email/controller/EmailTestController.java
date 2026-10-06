package com.baraigad.common.email.controller;

import com.baraigad.common.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping("/api/test-email")
    public String sendTestEmail() {

        emailService.sendHtmlEmail(
                "vinodsskale@gmail.com",
                "BaRaigad Email Test",
                "Congratulations! Spring Boot email configuration is working."
        );

        return "Email sent successfully";
    }
}