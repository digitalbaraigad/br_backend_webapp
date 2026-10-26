package com.baraigad.common.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

class EmailServiceImplTest {

    @Test
    void sendsMarathiSubjectUsingUtf8MimeEncoding() throws Exception {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(message);

        EmailServiceImpl service = new EmailServiceImpl(mailSender);
        ReflectionTestUtils.setField(service, "fromEmail", "notifications@example.com");

        String subject = "बा रायगड परिवार - सहभागी नोंदणी प्राप्त";
        service.sendHtmlEmail("recipient@example.com", subject, "नमस्कार!");

        assertEquals(subject, message.getSubject());
        verify(mailSender).send(message);
    }
}
