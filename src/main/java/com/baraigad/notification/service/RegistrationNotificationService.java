package com.baraigad.notification.service;

import com.baraigad.activity.entity.ActivityMaster;
import com.baraigad.common.email.service.EmailService;
import com.baraigad.dynamicform.dto.DynamicFormFieldDto;
import com.baraigad.dynamicform.entity.DynamicForm;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Configurable confirmation notifications for registrations and public forms. */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationNotificationService {
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    @Value("${baraigad.volunteer-registration-notification.enabled:true}") private boolean volunteerEnabled;
    @Value("${baraigad.volunteer-registration-notification.email-enabled:true}") private boolean volunteerEmailEnabled;
    @Value("${baraigad.volunteer-registration-notification.whatsapp-enabled:true}") private boolean volunteerWhatsAppEnabled;
    @Value("${baraigad.volunteer-registration-notification.email-to:}") private String volunteerEmailTo;
    @Value("${baraigad.volunteer-registration-notification.whatsapp-to:}") private String volunteerWhatsAppTo;
    @Value("${baraigad.volunteer-registration-notification.email-subject:Ba Raigad Parivar - Volunteer registration received}") private String volunteerEmailSubject;
    @Value("${baraigad.volunteer-registration-notification.email-template:Hello {name},<br/><br/>Thank you for registering as a volunteer with Ba Raigad Parivar.<br/><br/>Mobile: {mobile}<br/>District: {district}<br/>Taluka: {taluka}<br/><br/>Ba Raigad Parivar}") private String volunteerEmailTemplate;
    @Value("${baraigad.volunteer-registration-notification.whatsapp-template:Hello {name}, thank you for registering as a Ba Raigad Parivar volunteer.}") private String volunteerWhatsAppTemplate;
    @Value("${baraigad.volunteer-registration-notification.whatsapp-template-name:volunteer_join}") private String volunteerWhatsAppTemplateName;
    @Value("${baraigad.volunteer-registration-notification.whatsapp-template-language:mr}") private String volunteerWhatsAppTemplateLanguage;

    @Value("${baraigad.dynamic-form-submission-notification.enabled:true}") private boolean dynamicFormEnabled;
    @Value("${baraigad.dynamic-form-submission-notification.email-enabled:true}") private boolean dynamicFormEmailEnabled;
    @Value("${baraigad.dynamic-form-submission-notification.whatsapp-enabled:true}") private boolean dynamicFormWhatsAppEnabled;
    @Value("${baraigad.dynamic-form-submission-notification.email-to:}") private String dynamicFormEmailTo;
    @Value("${baraigad.dynamic-form-submission-notification.whatsapp-to:}") private String dynamicFormWhatsAppTo;
    @Value("${baraigad.dynamic-form-submission-notification.email-subject:Ba Raigad Parivar - {formTitle} submission received}") private String dynamicFormEmailSubject;
    @Value("${baraigad.dynamic-form-submission-notification.email-template:Hello {name},<br/><br/>We received your {formTitle} submission.<br/><br/>{details}<br/><br/>Ba Raigad Parivar}") private String dynamicFormEmailTemplate;
    @Value("${baraigad.dynamic-form-submission-notification.whatsapp-template:Hello {name}, we received your {formTitle} submission. Details:\n{details}") private String dynamicFormWhatsAppTemplate;
    @Value("${baraigad.dynamic-form-submission-notification.whatsapp-template-name:dynamic_form}") private String dynamicFormWhatsAppTemplateName;
    @Value("${baraigad.dynamic-form-submission-notification.whatsapp-template-language:mr}") private String dynamicFormWhatsAppTemplateLanguage;

    @Value("${baraigad.mohim-registration-notification.enabled:true}") private boolean mohimEnabled;
    @Value("${baraigad.mohim-registration-notification.email-enabled:true}") private boolean mohimEmailEnabled;
    @Value("${baraigad.mohim-registration-notification.whatsapp-enabled:true}") private boolean mohimWhatsAppEnabled;
    @Value("${baraigad.mohim-registration-notification.email-to:}") private String mohimEmailTo;
    @Value("${baraigad.mohim-registration-notification.whatsapp-to:}") private String mohimWhatsAppTo;
    @Value("${baraigad.mohim-registration-notification.email-subject:Ba Raigad Parivar - Mohim registration confirmed}") private String mohimEmailSubject;
    @Value("${baraigad.mohim-registration-notification.email-template:Hello {name},<br/><br/>Your registration for {activity} has been received.<br/><br/>Date: {date}<br/>Location: {location}<br/><br/>Ba Raigad Parivar}") private String mohimEmailTemplate;
    @Value("${baraigad.mohim-registration-notification.whatsapp-template:Hello {name}, your registration for {activity} has been received. Date: {date}. Location: {location}.}") private String mohimWhatsAppTemplate;
    @Value("${baraigad.mohim-registration-notification.whatsapp-template-name:mohim_registration_confirmation}") private String mohimWhatsAppTemplateName;
    @Value("${baraigad.mohim-registration-notification.whatsapp-template-language:mr}") private String mohimWhatsAppTemplateLanguage;

    @Async
    public void notifyVolunteerRegistration(Volunteer volunteer) {
        Map<String, String> values = volunteerValues(volunteer);
        notifyChannels("volunteer registration", volunteerEnabled, volunteerEmailEnabled, volunteerWhatsAppEnabled,
                volunteerEmailTo, volunteerWhatsAppTo, values.get("email"), values.get("mobile"),
                volunteerEmailSubject, volunteerEmailTemplate, volunteerWhatsAppTemplate,
                volunteerWhatsAppTemplateName, volunteerWhatsAppTemplateLanguage, values);
    }

    @Async
    public void notifyDynamicFormSubmission(DynamicForm form, List<DynamicFormFieldDto> fields, Map<String, String> responses) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("formTitle", safe(form.getTitle()));
        values.put("name", "Ba Raigad member");
        StringBuilder textDetails = new StringBuilder();
        StringBuilder htmlDetails = new StringBuilder();
        for (DynamicFormFieldDto field : fields) {
            if (!field.isIncludeInPdf()) continue;
            String label = safe(field.getLabel());
            String value = safe(responses.get(field.getId()));
            if (label.isBlank() || value.isBlank()) continue;
            if (isEmail(field)) values.put("email", value.trim());
            if (isMobile(field)) values.put("mobile", value.trim());
            if (isName(field)) values.put("name", value.trim());
            if (textDetails.length() > 0) textDetails.append("\n");
            textDetails.append(label).append(": ").append(value);
            if (htmlDetails.length() > 0) htmlDetails.append("<br/>");
            htmlDetails.append(HtmlUtils.htmlEscape(label)).append(": ").append(HtmlUtils.htmlEscape(value));
        }
        values.put("details", textDetails.toString());
        Map<String, String> htmlValues = new LinkedHashMap<>(values);
        htmlValues.replaceAll((key, value) -> "details".equals(key) ? htmlDetails.toString() : HtmlUtils.htmlEscape(value));
        notifyChannels("dynamic form submission", dynamicFormEnabled, dynamicFormEmailEnabled, dynamicFormWhatsAppEnabled,
                dynamicFormEmailTo, dynamicFormWhatsAppTo, values.get("email"), values.get("mobile"),
                dynamicFormEmailSubject, dynamicFormEmailTemplate, dynamicFormWhatsAppTemplate,
                dynamicFormWhatsAppTemplateName, dynamicFormWhatsAppTemplateLanguage, values, htmlValues);
    }

    @Async
    public void notifyMohimRegistration(Volunteer volunteer, ActivityMaster activity) {
        Map<String, String> values = volunteerValues(volunteer);
        values.put("activity", safe(activity.getActivityName()));
        values.put("date", activity.getStartDate() == null ? "To be announced" : activity.getStartDate().toString());
        values.put("location", safe(activity.getLocation()));
        notifyChannels("mohim registration", mohimEnabled, mohimEmailEnabled, mohimWhatsAppEnabled,
                mohimEmailTo, mohimWhatsAppTo, values.get("email"), values.get("mobile"),
                mohimEmailSubject, mohimEmailTemplate, mohimWhatsAppTemplate,
                mohimWhatsAppTemplateName, mohimWhatsAppTemplateLanguage, values);
    }

    private void notifyChannels(String event, boolean enabled, boolean emailEnabled, boolean whatsAppEnabled,
            String configuredEmail, String configuredWhatsApp, String fallbackEmail, String fallbackMobile,
            String emailSubject, String emailTemplate, String whatsAppTemplate, String whatsAppTemplateName,
            String whatsAppTemplateLanguage, Map<String, String> values) {
        Map<String, String> htmlValues = new LinkedHashMap<>(values);
        htmlValues.replaceAll((key, value) -> HtmlUtils.htmlEscape(value));
        notifyChannels(event, enabled, emailEnabled, whatsAppEnabled, configuredEmail, configuredWhatsApp,
                fallbackEmail, fallbackMobile, emailSubject, emailTemplate, whatsAppTemplate, whatsAppTemplateName,
                whatsAppTemplateLanguage, values, htmlValues);
    }

    private void notifyChannels(String event, boolean enabled, boolean emailEnabled, boolean whatsAppEnabled,
            String configuredEmail, String configuredWhatsApp, String fallbackEmail, String fallbackMobile,
            String emailSubject, String emailTemplate, String whatsAppTemplate, String whatsAppTemplateName,
            String whatsAppTemplateLanguage, Map<String, String> textValues,
            Map<String, String> htmlValues) {
        if (!enabled) return;
        String emailRecipient = preferred(configuredEmail, fallbackEmail);
        String whatsAppRecipient = preferred(configuredWhatsApp, fallbackMobile);
        if (emailEnabled && StringUtils.hasText(emailRecipient)) {
            try { emailService.sendHtmlEmail(emailRecipient, render(emailSubject, textValues), render(emailTemplate, htmlValues)); }
            catch (Exception exception) { log.warn("{} email could not be sent to {}", event, emailRecipient, exception); }
        }
        if (whatsAppEnabled && StringUtils.hasText(whatsAppRecipient)) {
            try { whatsAppService.sendTemplate(whatsAppRecipient.replaceAll("\\D", ""), whatsAppTemplateName, whatsAppTemplateLanguage, templateParameters(event, textValues)); }
            catch (Exception exception) { log.warn("{} WhatsApp message could not be sent to {}", event, whatsAppRecipient, exception); }
        }
    }

    private Map<String, String> templateParameters(String event, Map<String, String> values) {
        return switch (event) {
            case "volunteer registration" -> selected(values, "name", "mobile", "district", "taluka");
            case "dynamic form submission" -> {
                Map<String, String> parameters = new LinkedHashMap<>();
                parameters.put("name", safe(values.get("name")));
                parameters.put("form_title", safe(values.get("formTitle")));
                parameters.put("details", safe(values.get("details")));
                yield parameters;
            }
            case "mohim registration" -> selected(values, "name", "activity", "date", "location");
            default -> throw new IllegalArgumentException("Unsupported WhatsApp template notification event: " + event);
        };
    }

    private Map<String, String> selected(Map<String, String> values, String... keys) {
        Map<String, String> selected = new LinkedHashMap<>();
        for (String key : keys) selected.put(key, safe(values.get(key)));
        return selected;
    }

    private Map<String, String> volunteerValues(Volunteer volunteer) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("name", String.join(" ", List.of(safe(volunteer.getVolunteerFName()), safe(volunteer.getVolunteerMName()), safe(volunteer.getVolunteerLName())).stream().filter(value -> !value.isBlank()).toList()));
        values.put("email", safe(volunteer.getVolunteerEmail())); values.put("mobile", safe(volunteer.getVolunteerMobile()));
        values.put("city", safe(volunteer.getVolunteerCity())); values.put("district", safe(volunteer.getVolunteerDistrict()));
        values.put("taluka", safe(volunteer.getVolunteerTaluka())); values.put("occupation", safe(volunteer.getVolunteerOccupation()));
        values.put("availability", safe(volunteer.getVolunteerAvailability()));
        return values;
    }

    private boolean isEmail(DynamicFormFieldDto field) { return "email".equalsIgnoreCase(field.getType()) || normalized(field.getLabel()).contains("email"); }
    private boolean isMobile(DynamicFormFieldDto field) {
        String label = safe(field.getLabel()).toLowerCase();
        return "tel".equalsIgnoreCase(field.getType())
                || normalized(label).matches(".*(mobile|phone|contactnumber|whatsapp).*")
                || label.contains("मोबाईल") || label.contains("दूरध्वनी");
    }
    private boolean isName(DynamicFormFieldDto field) {
        String label = safe(field.getLabel()).toLowerCase();
        String normalizedLabel = normalized(label);
        return normalizedLabel.equals("name") || normalizedLabel.contains("fullname")
                || label.contains("पूर्ण नाव") || label.contains("संपूर्ण नाव");
    }
    private String preferred(String configured, String fallback) { return StringUtils.hasText(configured) ? configured.trim() : safe(fallback).trim(); }
    private String render(String template, Map<String, String> values) { String result = safe(template); for (Map.Entry<String, String> entry : values.entrySet()) result = result.replace("{" + entry.getKey() + "}", safe(entry.getValue())); return result; }
    private String normalized(String value) { return safe(value).toLowerCase().replaceAll("[^a-z0-9]", ""); }
    private String safe(String value) { return value == null ? "" : value; }
}
