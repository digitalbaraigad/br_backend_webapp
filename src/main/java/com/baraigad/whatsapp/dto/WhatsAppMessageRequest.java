package com.baraigad.whatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppMessageRequest {

    private String messaging_product;
    private String to;
    private String type;
    private Text text;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        private boolean preview_url;
        private String body;
    }
}