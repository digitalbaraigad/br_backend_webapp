package com.baraigad.volunteer.service;

import com.baraigad.common.email.service.EmailService;
import com.baraigad.dynamicform.dto.DynamicFormFieldDto;
import com.baraigad.dynamicform.entity.DynamicForm;
import com.baraigad.dynamicform.entity.DynamicFormSubmission;
import com.baraigad.dynamicform.repository.DynamicFormRepository;
import com.baraigad.dynamicform.repository.DynamicFormSubmissionRepository;
import com.baraigad.participant.repository.ParticipantRepository;
import com.baraigad.volunteer.repository.VolunteerRepository;
import com.baraigad.whatsapp.service.WhatsAppService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Uses one combined recipient list for the birthday count, email and WhatsApp wishes. */
@Service
@RequiredArgsConstructor
@Slf4j
public class BirthdayEmailServiceImpl implements BirthdayEmailService {
    private final VolunteerRepository volunteerRepository;
    private final ParticipantRepository participantRepository;
    private final DynamicFormRepository formRepository;
    private final DynamicFormSubmissionRepository submissionRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    @Value("${baraigad.birthday-notification.enabled:true}") private boolean enabled;
    @Value("${baraigad.birthday-notification.email-enabled:true}") private boolean emailEnabled;
    @Value("${baraigad.birthday-notification.whatsapp-enabled:true}") private boolean whatsAppEnabled;
    @Value("${baraigad.birthday-notification.email-subject:Happy Birthday from Ba Raigad Parivar}") private String emailSubject;
    @Value("${baraigad.birthday-notification.email-template:Dear {name},<br/><br/>Wishing you a very happy birthday!<br/><br/>Ba Raigad Parivar}") private String emailTemplate;
    @Value("${baraigad.birthday-notification.whatsapp-template:Dear {name}, Happy Birthday from Ba Raigad Parivar!}") private String whatsAppTemplate;

    @Override
    @Transactional(readOnly = true)
    public void sendBirthdayEmails() {
        if (!enabled) return;
        List<BirthdayRecipient> recipients = todaysRecipients();
        log.info("Sending birthday wishes to {} unique recipient(s).", recipients.size());
        for (BirthdayRecipient recipient : recipients) {
            if (emailEnabled && StringUtils.hasText(recipient.email())) {
                try { emailService.sendHtmlEmail(recipient.email(), render(emailSubject, recipient), render(emailTemplate, recipient)); }
                catch (Exception exception) { log.warn("Birthday email could not be sent to {}.", recipient.email(), exception); }
            }
            if (whatsAppEnabled && StringUtils.hasText(recipient.mobile())) {
                try { whatsAppService.sendMessage(recipient.mobile(), render(whatsAppTemplate, recipient)); }
                catch (Exception exception) { log.warn("Birthday WhatsApp message could not be sent to {}.", recipient.mobile(), exception); }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getTodaysBirthdayCount() { return todaysRecipients().size(); }
    @Override public void sendHtmlEmail(String to, String subject, String htmlContent) { emailService.sendHtmlEmail(to, subject, htmlContent); }

    private List<BirthdayRecipient> todaysRecipients() {
        LocalDate today = LocalDate.now();
        Map<String, BirthdayRecipient> recipients = new LinkedHashMap<>();
        volunteerRepository.findByDelFlgFalse().stream().filter(v -> isBirthdayToday(v.getVolunteerDOB(), today))
                .forEach(v -> add(recipients, new BirthdayRecipient(fullName(v.getVolunteerFName(), v.getVolunteerMName(), v.getVolunteerLName()), v.getVolunteerEmail(), v.getVolunteerMobile(), "volunteer:" + v.getVolunteerId())));
        participantRepository.findAll().stream().filter(p -> isBirthdayToday(p.getDob(), today))
                .forEach(p -> add(recipients, new BirthdayRecipient(fullName(p.getName(), p.getSurname()), p.getEmailId(), p.getMobileNumber(), "participant:" + p.getParticipantId())));

        Map<Long, DynamicForm> forms = new HashMap<>();
        for (DynamicFormSubmission submission : submissionRepository.findAllByOrderBySubmittedAtDesc()) {
            DynamicForm form = forms.computeIfAbsent(submission.getFormId(), id -> formRepository.findById(id).orElse(null));
            if (form == null) continue;
            BirthdayRecipient recipient = recipientFromSubmission(form, submission, today);
            if (recipient != null) add(recipients, recipient);
        }
        return List.copyOf(recipients.values());
    }

    private void add(Map<String, BirthdayRecipient> recipients, BirthdayRecipient recipient) {
        String key = normalizeMobile(recipient.mobile());
        if (key.isBlank()) key = normalizeEmail(recipient.email());
        if (key.isBlank()) key = recipient.fallbackKey();
        recipients.putIfAbsent(key, recipient);
    }

    private BirthdayRecipient recipientFromSubmission(DynamicForm form, DynamicFormSubmission submission, LocalDate today) {
        try {
            List<DynamicFormFieldDto> fields = objectMapper.readValue(form.getFieldsJson(), new TypeReference<List<DynamicFormFieldDto>>() {});
            Map<String, String> responses = objectMapper.readValue(submission.getResponsesJson(), new TypeReference<Map<String, String>>() {});
            LocalDate dob = null; String name = ""; String email = ""; String mobile = "";
            for (DynamicFormFieldDto field : fields) {
                String value = responses.get(field.getId());
                if (!StringUtils.hasText(value)) continue;
                String label = normalized(field.getLabel());
                String type = field.getType() == null ? "" : field.getType().toLowerCase(Locale.ROOT);
                if (isDateOfBirth(label, type)) dob = parseDate(value);
                if (name.isBlank() && isName(label)) name = value.trim();
                if (email.isBlank() && ("email".equals(type) || label.contains("email") || label.contains("ईमेल"))) email = value.trim();
                if (mobile.isBlank() && isMobile(label, type)) mobile = value.trim();
            }
            return isBirthdayToday(dob, today) ? new BirthdayRecipient(name.isBlank() ? "Ba Raigad member" : name, email, mobile, "form:" + submission.getSubmissionId()) : null;
        } catch (Exception exception) {
            log.warn("Birthday data could not be read from dynamic form submission {}.", submission.getSubmissionId(), exception);
            return null;
        }
    }

    private boolean isDateOfBirth(String label, String type) { return ("date".equals(type) && (label.contains("birth") || label.contains("dob") || label.contains("जन्म"))) || label.contains("dateofbirth") || label.contains("birthdate") || label.contains("जन्मतारीख"); }
    private boolean isName(String label) { return label.contains("fullname") || label.equals("name") || label.contains("पूर्णनाव") || label.contains("संपूर्णनाव"); }
    private boolean isMobile(String label, String type) { return "tel".equals(type) || label.contains("mobile") || label.contains("phone") || label.contains("contactnumber") || label.contains("मोबाईल") || label.contains("फोन"); }
    private boolean isBirthdayToday(LocalDate dob, LocalDate today) { return dob != null && dob.getMonthValue() == today.getMonthValue() && dob.getDayOfMonth() == today.getDayOfMonth(); }
    private LocalDate parseDate(String value) { for (DateTimeFormatter formatter : List.of(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ofPattern("dd-MM-uuuu"), DateTimeFormatter.ofPattern("dd/MM/uuuu"))) try { return LocalDate.parse(value.trim(), formatter); } catch (DateTimeParseException ignored) { } return null; }
    private String render(String template, BirthdayRecipient recipient) { return template.replace("{name}", recipient.name()).replace("{email}", safe(recipient.email())).replace("{mobile}", safe(recipient.mobile())); }
    private String normalized(String value) { return value == null ? "" : value.toLowerCase(Locale.ROOT).replaceAll("[\\s:()\\-_/]", ""); }
    private String fullName(String... values) { return Arrays.stream(values).filter(StringUtils::hasText).map(String::trim).reduce((a, b) -> a + " " + b).orElse("Ba Raigad member"); }
    private String normalizeMobile(String value) { String digits = value == null ? "" : value.replaceAll("\\D", ""); return digits.length() >= 10 ? digits.substring(digits.length() - 10) : ""; }
    private String normalizeEmail(String value) { return value == null ? "" : value.trim().toLowerCase(Locale.ROOT); }
    private String safe(String value) { return value == null ? "" : value; }
    private record BirthdayRecipient(String name, String email, String mobile, String fallbackKey) { }
}
