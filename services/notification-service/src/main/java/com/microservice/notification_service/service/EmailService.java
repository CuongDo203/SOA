package com.microservice.notification_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     *
     * @param to  			The recipent's email address
     * @param subject		The subject of email
     * @param text			The body of the email, can be html or plain text
     * @param isHtml		whether the email body is html or plain text
     * @param attachment	An optional file attachment, can be null
     */
    public void sendEmail(String to, String subject, String text, boolean isHtml, File attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);

            //add attachment if provide
            if(attachment != null) {
                FileSystemResource fileSystemResource = new FileSystemResource(attachment);
                helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

            }
            javaMailSender.send(message);
            log.info("Email send successfully to {}", to);
        }
        catch(MessagingException e) {
            log.error("Fail to send email to {}",to,e.getMessage());
        }
    }

}
