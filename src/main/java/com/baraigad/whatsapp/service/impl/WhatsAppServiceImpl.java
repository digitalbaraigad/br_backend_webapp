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

import java.util.HashMap;
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

        if (!StringUtils.hasText(accessToken)) {
            throw new IllegalStateException(
                    "WhatsApp notifications are not configured. Set WHATSAPP_ACCESS_TOKEN in the backend environment.");
        }
        if (!StringUtils.hasText(apiUrl)) {
            throw new IllegalStateException(
                    "WhatsApp notifications are not configured. Set WHATSAPP_API_URL in the backend environment.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", normalizeMobileNumber(mobileNo));
        request.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);

        request.put("text", text);

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

    private String normalizeMobileNumber(String mobileNo) {
        String digits = mobileNo == null ? "" : mobileNo.replaceAll("\\D", "");
        return digits.length() == 10 ? "91" + digits : digits;
    }
}
