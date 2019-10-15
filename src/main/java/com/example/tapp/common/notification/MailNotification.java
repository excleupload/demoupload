package com.example.tapp.common.notification;

import java.util.function.BiConsumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.tapp.common.notification.mail.TappMailSender;
import com.example.tapp.common.notification.mail.builder.MailMessage;
import com.example.tapp.common.notification.mail.builder.MailMessageBuilder;
import com.example.tapp.common.notification.template.Template;

@Component
public class MailNotification {

    private static final Logger log = LoggerFactory.getLogger(MailNotification.class);

    @Autowired
    private TappMailSender sender;

    public void sendForgotPassword(String to, Object[] args) {
        StringBuilder template = new StringBuilder(Template.getForgotPasswordTemplate());
        processTemplate.accept(template, args);
        MailMessage message = new MailMessageBuilder().to(to).subject(Template.FP_MAIL_SUBJECT)
                .body(template.toString()).isHtml(true).build();
        try {
            sender.send(message);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

    }


    public void sendMailForNotication(String to, String subjectList, String htmlbody)
    {
        MailMessage message=new MailMessageBuilder().to(to).subject(subjectList).body(htmlbody).isHtml(true).build();
        try {
            sender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    private BiConsumer<StringBuilder, Object[]> processTemplate = (sb, args) -> {
        String pattern = "";
        for (int i = 0; i < args.length; i++) {
            pattern = "{" + i + "}";
            int index = sb.indexOf(pattern);
            if (index == -1)
                return;
            sb.replace(index, index + pattern.length(), args[i] == null ? "" : (String) args[i]);
        }
    };

//    public void sendDeactiveMail(String to, Object[] args) {
//        StringBuilder template = new StringBuilder(Template.getdeactiveMailTemplate());
//        processTemplate.accept(template, args);
//        MailMessage message = new MailMessageBuilder().to(to).subject(Template.DEACTIVE_MAIL_SUBJECT)
//                .body(template.toString()).isHtml(true).build();
//        try {
//            sender.send(message);
//        } catch (Exception ex) {
//            log.info(ex.getMessage());
//        }
//
//    }

}