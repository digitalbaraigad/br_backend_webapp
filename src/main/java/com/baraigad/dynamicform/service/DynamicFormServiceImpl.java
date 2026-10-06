package com.baraigad.dynamicform.service;

import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.dynamicform.dto.*;
import com.baraigad.dynamicform.entity.DynamicForm;
import com.baraigad.dynamicform.entity.DynamicFormSubmission;
import com.baraigad.dynamicform.repository.DynamicFormRepository;
import com.baraigad.dynamicform.repository.DynamicFormSubmissionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DynamicFormServiceImpl implements DynamicFormService {
    private final DynamicFormRepository formRepository;
    private final DynamicFormSubmissionRepository submissionRepository;
    private final ObjectMapper objectMapper;

    @Override public DynamicFormDto create(DynamicFormDto dto) {
        DynamicForm form = new DynamicForm();
        form.setPublicId(UUID.randomUUID().toString().replace("-", ""));
        form.setTitle(dto.getTitle().trim()); form.setHeaderImageUrl(normalizeImageUrl(dto.getHeaderImageUrl())); form.setDescription(dto.getDescription());
        form.setConsentText(dto.getConsentText().trim()); form.setStatus(dto.getStatus() == null ? "PUBLISHED" : dto.getStatus());
        if (dto.getExpiryDate() != null && dto.getExpiryDate().isBefore(LocalDate.now())) throw new IllegalArgumentException("Expiry date cannot be in the past");
        form.setExpiryDate(dto.getExpiryDate());
        form.setFieldsJson(write(dto.getFields())); form.setCreatedAt(LocalDateTime.now());
        return toDto(formRepository.save(form));
    }
    @Override @Transactional(readOnly = true) public PagedResponseDto<DynamicFormDto> getAll(Integer pageNo, Integer pageSize) {
        int safePageNo = Math.max(1, pageNo == null ? 1 : pageNo);
        int safePageSize = Math.max(1, Math.min(pageSize == null ? 10 : pageSize, 100));
        Pageable pageable = PageRequest.of(safePageNo - 1, safePageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PaginationUtil.build(formRepository.findAll(pageable), this::toDto);
    }
    @Override @Transactional(readOnly = true) public DynamicFormDto getPublicForm(String publicId) {
        DynamicForm form = findPublic(publicId);
        ensurePublicFormIsAvailable(form);
        return toDto(form);
    }
    @Override @Transactional public DynamicFormSubmissionDto submit(String publicId, DynamicFormSubmissionRequest request) {
        DynamicForm form = findPublic(publicId);
        ensurePublicFormIsAvailable(form);
        List<DynamicFormFieldDto> fields = fields(form);
        for (DynamicFormFieldDto field : fields) {
            String value = request.getResponses().get(field.getId());
            if (field.isRequired() && (value == null || value.trim().isEmpty())) throw new IllegalArgumentException(field.getLabel() + " is required");
        }
        DynamicFormSubmission submission = new DynamicFormSubmission();
        submission.setFormId(form.getFormId()); submission.setResponsesJson(write(request.getResponses()));
        submission.setConsentAccepted(request.isConsentAccepted()); submission.setSubmittedAt(LocalDateTime.now());
        return toSubmissionDto(submissionRepository.save(submission));
    }
    @Override @Transactional(readOnly = true) public List<DynamicFormSubmissionDto> getSubmissions(Long formId) {
        if (!formRepository.existsById(formId)) throw new ResourceNotFoundException("Form not found");
        return submissionRepository.findByFormIdOrderBySubmittedAtDesc(formId).stream().map(this::toSubmissionDto).toList();
    }
    @Override @Transactional(readOnly = true)
    public Map<String, String> findSharedFormProfileByMobile(String mobileNumber) {
        String normalizedMobile = normalizeMobile(mobileNumber);
        if (normalizedMobile.length() != 10) return Map.of();
        Map<String, DynamicForm> forms = new HashMap<>();
        Map<String, String> profile = new LinkedHashMap<>();
        for (DynamicFormSubmission submission : submissionRepository.findAllByOrderBySubmittedAtDesc()) {
            DynamicForm form = forms.computeIfAbsent(String.valueOf(submission.getFormId()),
                    ignored -> formRepository.findById(submission.getFormId()).orElse(null));
            if (form == null) continue;
            List<DynamicFormFieldDto> formFields = fields(form);
            Map<String, String> submittedResponses = readMap(submission.getResponsesJson());
            boolean mobileMatches = formFields.stream().anyMatch(field ->
                    isMobileField(field) && normalizedMobile.equals(normalizeMobile(submittedResponses.get(field.getId()))));
            if (!mobileMatches) continue;
            for (DynamicFormFieldDto field : formFields) {
                if ("image".equalsIgnoreCase(field.getType())) continue;
                String value = submittedResponses.get(field.getId());
                String key = profileKey(field);
                if (value != null && !value.isBlank() && !key.isBlank()) profile.putIfAbsent(key, value.trim());
            }
        }
        return profile;
    }
    private DynamicForm findPublic(String publicId) { return formRepository.findByPublicId(publicId).orElseThrow(() -> new ResourceNotFoundException("Form not found")); }
    private void ensurePublicFormIsAvailable(DynamicForm form) { if (!"PUBLISHED".equalsIgnoreCase(form.getStatus())) throw new ResourceNotFoundException("This form is not currently available"); if (form.getExpiryDate() != null && form.getExpiryDate().isBefore(LocalDate.now())) throw new ResourceNotFoundException("This form has expired"); }
    private DynamicFormDto toDto(DynamicForm form) { DynamicFormDto dto=new DynamicFormDto(); dto.setFormId(form.getFormId()); dto.setPublicId(form.getPublicId()); dto.setTitle(form.getTitle()); dto.setHeaderImageUrl(form.getHeaderImageUrl()); dto.setDescription(form.getDescription()); dto.setConsentText(form.getConsentText()); dto.setStatus(form.getStatus()); dto.setExpiryDate(form.getExpiryDate()); dto.setCreatedAt(form.getCreatedAt()); dto.setFields(fields(form)); dto.setTotalResponses(submissionRepository.countByFormId(form.getFormId())); return dto; }
    private DynamicFormSubmissionDto toSubmissionDto(DynamicFormSubmission item) { DynamicFormSubmissionDto dto=new DynamicFormSubmissionDto(); dto.setSubmissionId(item.getSubmissionId()); dto.setFormId(item.getFormId()); dto.setConsentAccepted(item.isConsentAccepted()); dto.setSubmittedAt(item.getSubmittedAt()); dto.setResponses(readMap(item.getResponsesJson())); return dto; }
    private List<DynamicFormFieldDto> fields(DynamicForm form) { try { return objectMapper.readValue(form.getFieldsJson(), new TypeReference<List<DynamicFormFieldDto>>() {}); } catch (Exception error) { throw new IllegalStateException("Stored form fields could not be read", error); } }
    private String normalizeImageUrl(String imageUrl) { if (imageUrl == null || imageUrl.isBlank()) return null; String value = imageUrl.trim(); if (value.length() > 2048) throw new IllegalArgumentException("Top image URL must be 2048 characters or fewer"); if (!(value.startsWith("https://") || value.startsWith("http://") || value.startsWith("/"))) throw new IllegalArgumentException("Top image URL must start with https://, http://, or /"); return value; }
    private boolean isMobileField(DynamicFormFieldDto field) {
        String label = field.getLabel() == null ? "" : field.getLabel().toLowerCase();
        return "tel".equalsIgnoreCase(field.getType())
                || normalizeFieldKey(field.getLabel()).matches(".*(mobile|phone|contactnumber|whatsapp).*")
                || label.matches(".*(मोबाईल|फोन|संपर्क).*" );
    }
    private String profileKey(DynamicFormFieldDto field) {
        String label = field.getLabel() == null ? "" : field.getLabel().toLowerCase();
        if (label.matches(".*(full\\s*name|संपूर्ण\\s*नाव).*")) return "fullname";
        if (label.matches(".*(birth\\s*date|date\\s*of\\s*birth|\\bdob\\b|जन्म\\s*तारीख).*")) return "birthdate";
        if (label.matches(".*(\\bage\\b|वय).*")) return "age";
        if (label.matches(".*(travel|प्रवास).*")) return "travelfrom";
        if (label.matches(".*(weight|वजन).*")) return "weight";
        if (label.matches(".*(blood\\s*group|रक्तगट).*")) return "bloodgroup";
        if (label.matches(".*(emergency|संकटकालीन).*")) return "emergencycontact";
        if (label.matches(".*(residential\\s*address|राहण्याचे\\s*ठिकाण).*")) return "residentialaddress";
        if (label.matches(".*(resident\\s*in|तुम्ही\\s*राहता).*")) return "residentarea";
        if (label.matches(".*(village|मूळ\\s*गाव).*")) return "village";
        if (label.matches(".*(district|जिल्हा).*")) return "district";
        if (label.matches(".*(taluka|तालुका).*")) return "taluka";
        if (label.matches(".*(occupation|तुम्ही\\s*काय\\s*करता).*")) return "occupation";
        if (label.matches(".*(job|business|profession|नोकरी|व्यवसाय).*")) return "jobbusinessnature";
        if (label.matches(".*(trekking|climbing|rappelling|भटकंती).*")) return "trekkingexperience";
        if (label.matches(".*(social\\s*work|स्वराज्यकार्य).*")) return "socialwork";
        return normalizeFieldKey(field.getLabel());
    }
    private String normalizeFieldKey(String value) { return value == null ? "" : value.toLowerCase().replaceAll("[^a-z0-9]", ""); }
    private String normalizeMobile(String value) { return value == null ? "" : value.replaceAll("\\D", ""); }
    private Map<String,String> readMap(String json) { try { return objectMapper.readValue(json, new TypeReference<Map<String,String>>() {}); } catch (Exception error) { throw new IllegalStateException("Stored submission could not be read", error); } }
    private String write(Object value) { try { return objectMapper.writeValueAsString(value); } catch (Exception error) { throw new IllegalArgumentException("Form data could not be saved", error); } }
}
