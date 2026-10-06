package com.baraigad.participant.service;

import com.baraigad.common.email.service.EmailService;
import com.baraigad.participant.entity.Participant;
import com.baraigad.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantNotificationService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    @Value("${baraigad.participant-notification.enabled:false}")
    private boolean enabled;
    @Value("${baraigad.participant-notification.email-enabled:true}")
    private boolean emailEnabled;
    @Value("${baraigad.participant-notification.whatsapp-enabled:true}")
    private boolean whatsAppEnabled;
    @Value("${baraigad.participant-notification.email-to:}")
    private String configuredEmailRecipient;
    @Value("${baraigad.participant-notification.whatsapp-to:}")
    private String configuredWhatsAppRecipient;
    @Value("${baraigad.participant-notification.email-subject}")
    private String emailSubject;
    @Value("${baraigad.participant-notification.email-template}")
    private String emailTemplate;
    @Value("${baraigad.participant-notification.whatsapp-template}")
    private String whatsAppTemplate;

    /** Sends confirmation to the participant unless a configured team recipient overrides it. */
    @Async
    public void notifyParticipant(Participant participant) {
        if (!enabled) return;
        if (emailEnabled) {
            sendEmail(
                    StringUtils.hasText(configuredEmailRecipient) ? configuredEmailRecipient.trim() : participant.getEmailId(),
                    render(emailSubject, participant),
                    render(emailTemplate, participant));
        }
        if (whatsAppEnabled) {
            sendWhatsApp(
                    StringUtils.hasText(configuredWhatsAppRecipient) ? configuredWhatsAppRecipient.trim() : participant.getMobileNumber(),
                    render(whatsAppTemplate, participant));
        }
    }

    private void sendEmail(String recipient, String subject, String content) {
        if (!StringUtils.hasText(recipient)) return;
        try {
            emailService.sendHtmlEmail(recipient, subject, content);
        } catch (Exception exception) {
            log.warn("Participant notification email could not be sent to {}", recipient, exception);
        }
    }

    private void sendWhatsApp(String recipient, String message) {
        if (!StringUtils.hasText(recipient)) return;
        try {
            whatsAppService.sendMessage(recipient.replaceAll("\\D", ""), message);
        } catch (Exception exception) {
            log.warn("Participant notification WhatsApp message could not be sent to {}", recipient, exception);
        }
    }

    private String render(String template, Participant participant) {
        return template
                .replace("{name}", (safe(participant.getName()) + " " + safe(participant.getSurname())).trim())
                .replace("{email}", safe(participant.getEmailId()))
                .replace("{mobile}", safe(participant.getMobileNumber()))
                .replace("{district}", safe(participant.getDistrict()))
                .replace("{taluka}", safe(participant.getTaluka()))
                .replace("{interest}", safe(participant.getInterestedActivity()));
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
