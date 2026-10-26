package com.baraigad.bulkcommunication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class BulkDispatchRequest {
    @NotBlank private String channel;
    @NotEmpty private List<String> recipientKeys;
    private String templateCode;
    private Map<String, String> templateValues;
    private String emailSubject;
    private String emailBody;
    @NotNull private Boolean confirmed;

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public List<String> getRecipientKeys() { return recipientKeys; }
    public void setRecipientKeys(List<String> recipientKeys) { this.recipientKeys = recipientKeys; }
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    public Map<String, String> getTemplateValues() { return templateValues; }
    public void setTemplateValues(Map<String, String> templateValues) { this.templateValues = templateValues; }
    public String getEmailSubject() { return emailSubject; }
    public void setEmailSubject(String emailSubject) { this.emailSubject = emailSubject; }
    public String getEmailBody() { return emailBody; }
    public void setEmailBody(String emailBody) { this.emailBody = emailBody; }
    public Boolean getConfirmed() { return confirmed; }
    public void setConfirmed(Boolean confirmed) { this.confirmed = confirmed; }
}
