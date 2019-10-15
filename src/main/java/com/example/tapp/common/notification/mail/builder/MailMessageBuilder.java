package com.example.tapp.common.notification.mail.builder;

import java.util.Arrays;

public class MailMessageBuilder {

    private MailMessage message;

    public MailMessageBuilder() {
        this.message = new MailMessage();
    }

    public MailMessageBuilder to(String to) {
        message.setTo(to);
        return this;
    }

    public MailMessageBuilder subject(String subject) {
        message.setSubject(subject);
        return this;
    }

    public MailMessageBuilder body(String body) {
        message.setBody(body);
        return this;
    }

    public MailMessageBuilder isHtml(boolean html) {
        message.setHtml(html);
        return this;
    }

    public MailMessageBuilder attachment(Attachment... attachment) {
        message.getAttachments().addAll(Arrays.asList(attachment));
        return this;
    }

    public MailMessage build() {
        return this.message;
    }

}

