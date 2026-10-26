package com.baraigad.whatsapp.service.impl;

import com.baraigad.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppServiceImpl implements WhatsAppService {

    @Value("${whatsapp.api-url}")
    private String apiUrl;

    @Value("${whatsapp.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate;

    @Override
    public void sendMessage(String mobileNo,
                            String message) {
        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", normalizeMobileNumber(mobileNo));
        request.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);

        request.put("text", text);

        post(request);
    }

    @Override
    public void sendTemplate(String mobileNo, String templateName, String languageCode,
                             Map<String, String> bodyParameters) {
        if (!StringUtils.hasText(templateName)) {
            throw new IllegalArgumentException("A Meta WhatsApp template name is required.");
        }
        if (!StringUtils.hasText(languageCode)) {
            throw new IllegalArgumentException("A Meta WhatsApp template language code is required.");
        }

        List<Map<String, String>> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : bodyParameters.entrySet()) {
            if (!StringUtils.hasText(entry.getKey())) continue;
            Map<String, String> parameter = new LinkedHashMap<>();
            parameter.put("type", "text");
            parameter.put("parameter_name", entry.getKey());
            parameter.put("text", templateValue(entry.getValue()));
            parameters.add(parameter);
        }

        Map<String, Object> bodyComponent = new LinkedHashMap<>();
        bodyComponent.put("type", "body");
        bodyComponent.put("parameters", parameters);

        Map<String, Object> language = Map.of("code", languageCode);
        Map<String, Object> template = new LinkedHashMap<>();
        template.put("name", templateName);
        template.put("language", language);
        template.put("components", List.of(bodyComponent));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", normalizeMobileNumber(mobileNo));
        request.put("type", "template");
        request.put("template", template);

        post(request);
    }

    private void post(Map<String, Object> request) {
        validateConfiguration();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.POST,
                            entity,
                            String.class
                    );
            log.info("WhatsApp Response: {}", response.getBody());
        } catch (HttpClientErrorException.Unauthorized exception) {
            log.error("Meta WhatsApp API rejected the configured access token for {}.", apiUrl);
            throw new IllegalStateException(
                    "WhatsApp notifications could not be authorized. Replace WHATSAPP_ACCESS_TOKEN with a valid Meta access token for the configured phone number.",
                    exception);
        }
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(accessToken)) {
            throw new IllegalStateException(
                    "WhatsApp notifications are not configured. Set WHATSAPP_ACCESS_TOKEN in the backend environment.");
        }
        if (!StringUtils.hasText(apiUrl)) {
            throw new IllegalStateException(
                    "WhatsApp notifications are not configured. Set WHATSAPP_API_URL in the backend environment.");
        }
    }

    private String templateValue(String value) {
        if (!StringUtils.hasText(value)) return "-";
        // Meta template parameter text cannot contain newlines, tabs, or more than four consecutive spaces.
        return value.trim()
                .replaceAll("[\\r\\n\\t]+", "; ")
                .replaceAll(" {2,}", " ");
    }

    private String normalizeMobileNumber(String mobileNo) {
        String digits = mobileNo == null ? "" : mobileNo.replaceAll("\\D", "");
        return digits.length() == 10 ? "91" + digits : digits;
    }
}
