package com.example.tapp.common.notification.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.tapp.common.notification.mail.builder.MailMessage;

@Component
public class TappMailSender {

   
    @Autowired
    private JavaMailSender javaMailSender;

    public void send(MailMessage mailMessage) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mailMessage.getTo());
        helper.setSubject(mailMessage.getSubject());
        helper.setText(mailMessage.getBody(), mailMessage.isHtml());

        mailMessage.getAttachments().forEach(attachment -> {
            FileSystemResource file = new FileSystemResource(new File(attachment.getPath()));
            try {
                helper.addAttachment(attachment.getName(), file);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        javaMailSender.send(message);
    }

}
