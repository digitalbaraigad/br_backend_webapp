package com.baraigad.whatsapp.controller;

import com.baraigad.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsappService;

    @GetMapping("/test")
    public String testWhatsApp(@RequestParam String mobileNo) {

        whatsappService.sendTemplate(mobileNo, "hello_world", "en_US", Map.of());

        return "WhatsApp hello_world template submitted successfully";
    }
}
