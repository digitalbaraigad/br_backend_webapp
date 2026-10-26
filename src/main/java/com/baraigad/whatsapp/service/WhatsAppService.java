package com.baraigad.whatsapp.service;

import java.util.Map;

public interface WhatsAppService {

    void sendMessage(String mobileNo,
                     String message);

    /** Sends an approved Meta WhatsApp template using its named body variables. */
    void sendTemplate(String mobileNo,
                      String templateName,
                      String languageCode,
                      Map<String, String> bodyParameters);
}
