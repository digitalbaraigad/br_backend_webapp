package com.baraigad.whatsapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class WhatsAppServiceImplTest {

    @Test
    @SuppressWarnings("unchecked")
    void sendsApprovedTemplateWithNamedBodyParameters() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(eq("https://example.test/messages"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));
        WhatsAppServiceImpl service = new WhatsAppServiceImpl(restTemplate);
        ReflectionTestUtils.setField(service, "apiUrl", "https://example.test/messages");
        ReflectionTestUtils.setField(service, "accessToken", "test-token");

        Map<String, String> variables = new LinkedHashMap<>();
        variables.put("name", "Test Member");
        variables.put("interest", "Fort conservation\n\tand documentation");
        service.sendTemplate("98765 43210", "join_us_confirmation", "mr", variables);

        ArgumentCaptor<HttpEntity<Map<String, Object>>> requestCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        org.mockito.Mockito.verify(restTemplate).exchange(eq("https://example.test/messages"), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        Map<String, Object> request = requestCaptor.getValue().getBody();
        assertEquals("template", request.get("type"));
        assertEquals("919876543210", request.get("to"));
        Map<String, Object> template = (Map<String, Object>) request.get("template");
        assertEquals("join_us_confirmation", template.get("name"));
        assertEquals("mr", ((Map<String, String>) template.get("language")).get("code"));
        Map<String, Object> component = ((java.util.List<Map<String, Object>>) template.get("components")).get(0);
        Map<String, String> firstParameter = ((java.util.List<Map<String, String>>) component.get("parameters")).get(0);
        assertEquals("name", firstParameter.get("parameter_name"));
        assertEquals("Test Member", firstParameter.get("text"));
        Map<String, String> secondParameter = ((java.util.List<Map<String, String>>) component.get("parameters")).get(1);
        assertEquals("Fort conservation; and documentation", secondParameter.get("text"));
    }
}
