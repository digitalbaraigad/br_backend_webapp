package com.baraigad.bulkcommunication.service;

import com.baraigad.bulkcommunication.dto.*;
import com.baraigad.common.email.service.EmailService;
import com.baraigad.dynamicform.dto.DynamicFormFieldDto;
import com.baraigad.dynamicform.entity.DynamicForm;
import com.baraigad.dynamicform.entity.DynamicFormSubmission;
import com.baraigad.dynamicform.repository.DynamicFormRepository;
import com.baraigad.dynamicform.repository.DynamicFormSubmissionRepository;
import com.baraigad.participant.entity.Participant;
import com.baraigad.participant.repository.ParticipantRepository;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.repository.VolunteerRepository;
import com.baraigad.whatsapp.service.WhatsAppService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkCommunicationService {
    private static final int MAX_RECIPIENTS_PER_SEND = 500;
    private final ParticipantRepository participantRepository;
    private final VolunteerRepository volunteerRepository;
    private final DynamicFormRepository formRepository;
    private final DynamicFormSubmissionRepository submissionRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final ObjectMapper objectMapper;

    @Value("${baraigad.bulk-communication.mohim-template-name:mohim_registration_confirmation}")
    private String mohimTemplateName;
    @Value("${baraigad.bulk-communication.trekking-template-name:trekking_announcement}")
    private String trekkingTemplateName;
    @Value("${baraigad.bulk-communication.social-work-template-name:social_work_announcement}")
    private String socialWorkTemplateName;
    @Value("${baraigad.bulk-communication.template-language:mr}")
    private String templateLanguage;

    public List<BulkRecipientDto> recipients(String requestedSource, Long formId) {
        String source = safe(requestedSource).trim().toUpperCase(Locale.ROOT);
        if (!List.of("ALL", "VOLUNTEERS", "PARTICIPANTS", "DYNAMIC_FORMS").contains(source)) {
            throw new IllegalArgumentException("Recipient source must be ALL, VOLUNTEERS, PARTICIPANTS or DYNAMIC_FORMS.");
        }
        List<BulkRecipientDto> recipients = new ArrayList<>();
        if ("ALL".equals(source) || "VOLUNTEERS".equals(source)) {
            volunteerRepository.findByDelFlgFalse().forEach(item -> recipients.add(volunteer(item)));
        }
        if ("ALL".equals(source) || "PARTICIPANTS".equals(source)) {
            participantRepository.findAll().forEach(item -> recipients.add(participant(item)));
        }
        if ("ALL".equals(source) || "DYNAMIC_FORMS".equals(source)) {
            Map<Long, DynamicForm> forms = formRepository.findAll().stream()
                    .collect(java.util.stream.Collectors.toMap(DynamicForm::getFormId, item -> item));
            submissionRepository.findAllByOrderBySubmittedAtDesc().stream()
                    .filter(item -> formId == null || Objects.equals(formId, item.getFormId()))
                    .forEach(item -> recipients.add(dynamic(item, forms.get(item.getFormId()))));
        }
        return recipients.stream()
                .sorted(Comparator.comparing(BulkRecipientDto::source).thenComparing(item -> safe(item.name()), String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<BulkTemplateDto> templates() {
        return definitions().values().stream().map(TemplateDefinition::dto).toList();
    }

    public BulkDispatchResultDto send(BulkDispatchRequest request) {
        if (!Boolean.TRUE.equals(request.getConfirmed())) {
            throw new IllegalArgumentException("Confirm the recipient count before sending a bulk communication.");
        }
        List<String> selected = request.getRecipientKeys().stream().filter(StringUtils::hasText).distinct().toList();
        if (selected.size() > MAX_RECIPIENTS_PER_SEND) {
            throw new IllegalArgumentException("A maximum of " + MAX_RECIPIENTS_PER_SEND + " recipients can be sent in one batch.");
        }
        String channel = safe(request.getChannel()).trim().toUpperCase(Locale.ROOT);
        if (!List.of("WHATSAPP", "EMAIL").contains(channel)) {
            throw new IllegalArgumentException("Channel must be WHATSAPP or EMAIL.");
        }
        if ("EMAIL".equals(channel) && (!StringUtils.hasText(request.getEmailSubject()) || !StringUtils.hasText(request.getEmailBody()))) {
            throw new IllegalArgumentException("Email subject and message are required.");
        }
        TemplateDefinition definition = null;
        if ("WHATSAPP".equals(channel)) {
            definition = definitions().get(safe(request.getTemplateCode()).trim().toUpperCase(Locale.ROOT));
            if (definition == null) throw new IllegalArgumentException("Select a valid approved WhatsApp campaign template.");
        }

        Map<String, BulkRecipientDto> available = recipients("ALL", null).stream()
                .collect(java.util.stream.Collectors.toMap(BulkRecipientDto::recipientKey, item -> item, (first, ignored) -> first));
        Set<String> deliveredTargets = new HashSet<>();
        List<String> notes = new ArrayList<>();
        int sent = 0, skipped = 0, failed = 0;
        for (String key : selected) {
            BulkRecipientDto recipient = available.get(key);
            if (recipient == null) { skipped++; note(notes, key + " is no longer available."); continue; }
            String target = "WHATSAPP".equals(channel) ? digits(recipient.mobile()) : safe(recipient.email()).trim().toLowerCase(Locale.ROOT);
            if (!StringUtils.hasText(target)) { skipped++; note(notes, recipient.name() + " has no " + ("WHATSAPP".equals(channel) ? "mobile number" : "email address") + "."); continue; }
            if (!deliveredTargets.add(target)) { skipped++; note(notes, "Duplicate " + target + " was skipped."); continue; }
            try {
                if ("WHATSAPP".equals(channel)) {
                    whatsAppService.sendTemplate(target, definition.templateName(), definition.languageCode(), templateParameters(definition, request.getTemplateValues(), recipient));
                } else {
                    Map<String, String> values = recipientValues(recipient, request.getTemplateValues());
                    emailService.sendHtmlEmail(target, renderText(request.getEmailSubject(), values), renderHtml(request.getEmailBody(), values));
                }
                sent++;
            } catch (Exception exception) {
                failed++;
                log.warn("Bulk {} could not be sent to {}", channel, target, exception);
                note(notes, "Could not send to " + recipient.name() + ".");
            }
        }
        return new BulkDispatchResultDto(selected.size(), sent, skipped, failed, notes);
    }

    private Map<String, TemplateDefinition> definitions() {
        Map<String, TemplateDefinition> items = new LinkedHashMap<>();
        items.put("MOHIM", new TemplateDefinition("MOHIM", "Mohim / programme", mohimTemplateName, templateLanguage, List.of("name", "activity", "date", "location")));
        items.put("TREKKING", new TemplateDefinition("TREKKING", "Trekking / भटकंती", trekkingTemplateName, templateLanguage, List.of("name", "activity", "date", "location")));
        items.put("SOCIAL_WORK", new TemplateDefinition("SOCIAL_WORK", "Social Work / सामाजिक उपक्रम", socialWorkTemplateName, templateLanguage, List.of("name", "activity", "date", "location")));
        return items;
    }

    private BulkRecipientDto participant(Participant item) {
        return new BulkRecipientDto("PARTICIPANT:" + item.getParticipantId(), "PARTICIPANTS", "Join Us participant",
                join(item.getName(), item.getSurname()), safe(item.getMobileNumber()), safe(item.getEmailId()), safe(item.getDistrict()), safe(item.getTaluka()), "");
    }

    private BulkRecipientDto volunteer(Volunteer item) {
        return new BulkRecipientDto("VOLUNTEER:" + item.getVolunteerId(), "VOLUNTEERS", "Volunteer",
                join(item.getVolunteerFName(), item.getVolunteerMName(), item.getVolunteerLName()), safe(item.getVolunteerMobile()), safe(item.getVolunteerEmail()), safe(item.getVolunteerDistrict()), safe(item.getVolunteerTaluka()), "");
    }

    private BulkRecipientDto dynamic(DynamicFormSubmission item, DynamicForm form) {
        Map<String, String> answers = answers(item, form);
        String name = find(answers, "name", "full name", "पूर्ण नाव", "संपूर्ण नाव", "नाव");
        String mobile = find(answers, "mobile", "phone", "contact", "whatsapp", "मोबाईल", "दूरध्वनी");
        String email = find(answers, "email", "ईमेल");
        String district = find(answers, "district", "जिल्हा");
        String taluka = find(answers, "taluka", "तालुका");
        return new BulkRecipientDto("DYNAMIC:" + item.getSubmissionId(), "DYNAMIC_FORMS", "Dynamic form submission",
                name, mobile, email, district, taluka, form == null ? "Dynamic form" : safe(form.getTitle()));
    }

    private Map<String, String> answers(DynamicFormSubmission item, DynamicForm form) {
        Map<String, String> responses = readMap(item.getResponsesJson());
        Map<String, String> values = new LinkedHashMap<>(responses);
        if (form == null) return values;
        for (DynamicFormFieldDto field : readFields(form.getFieldsJson())) {
            String value = responses.get(field.getId());
            if (StringUtils.hasText(value) && StringUtils.hasText(field.getLabel())) values.put(field.getLabel(), value);
        }
        return values;
    }

    private Map<String, String> readMap(String json) {
        try {
            Map<String, Object> raw = objectMapper.readValue(safe(json), new TypeReference<>() {});
            Map<String, String> result = new LinkedHashMap<>();
            raw.forEach((key, value) -> result.put(key, value == null ? "" : String.valueOf(value)));
            return result;
        } catch (Exception exception) { return Map.of(); }
    }

    private List<DynamicFormFieldDto> readFields(String json) {
        try { return objectMapper.readValue(safe(json), new TypeReference<>() {}); }
        catch (Exception exception) { return List.of(); }
    }

    private String find(Map<String, String> values, String... labels) {
        for (String label : labels) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                if (safe(entry.getKey()).toLowerCase(Locale.ROOT).contains(label.toLowerCase(Locale.ROOT)) && StringUtils.hasText(entry.getValue())) return entry.getValue().trim();
            }
        }
        return "";
    }

    private Map<String, String> templateParameters(TemplateDefinition definition, Map<String, String> common, BulkRecipientDto recipient) {
        Map<String, String> values = recipientValues(recipient, common);
        Map<String, String> parameters = new LinkedHashMap<>();
        for (String name : definition.variables()) parameters.put(name, safe(values.get(name)));
        return parameters;
    }

    private Map<String, String> recipientValues(BulkRecipientDto recipient, Map<String, String> common) {
        Map<String, String> values = new LinkedHashMap<>();
        if (common != null) common.forEach((key, value) -> values.put(safe(key), safe(value)));
        values.put("name", safe(recipient.name()));
        values.put("mobile", safe(recipient.mobile()));
        values.put("email", safe(recipient.email()));
        values.put("district", safe(recipient.district()));
        values.put("taluka", safe(recipient.taluka()));
        values.put("form_title", safe(recipient.formTitle()));
        return values;
    }

    private String renderText(String template, Map<String, String> values) {
        String result = safe(template);
        for (Map.Entry<String, String> entry : values.entrySet()) result = result.replace("{" + entry.getKey() + "}", safe(entry.getValue()));
        return result;
    }

    private String renderHtml(String template, Map<String, String> values) {
        Map<String, String> escaped = new LinkedHashMap<>();
        values.forEach((key, value) -> escaped.put(key, HtmlUtils.htmlEscape(safe(value))));
        return renderText(HtmlUtils.htmlEscape(safe(template)).replace("\n", "<br/>"), escaped);
    }

    private void note(List<String> notes, String message) { if (notes.size() < 25) notes.add(message); }
    private String digits(String value) { return safe(value).replaceAll("\\D", ""); }
    private String join(String... values) { return Arrays.stream(values).map(this::safe).filter(StringUtils::hasText).collect(java.util.stream.Collectors.joining(" ")); }
    private String safe(String value) { return value == null ? "" : value.trim(); }

    private record TemplateDefinition(String code, String label, String templateName, String languageCode, List<String> variables) {
        BulkTemplateDto dto() { return new BulkTemplateDto(code, label, templateName, languageCode, variables); }
    }
}
