package com.baraigad.whatsapp.controller;

import com.baraigad.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsappService;

    @GetMapping("/test")
    public String testWhatsApp() {

        whatsappService.sendMessage(
                "919503777850",
                "Hello Vinod, WhatsApp API is integrated successfully 🚩"
        );

        return "WhatsApp Message Sent Successfully";
    }
}