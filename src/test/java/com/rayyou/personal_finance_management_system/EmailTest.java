package com.rayyou.personal_finance_management_system;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("w1fki@mechanicspedia.com");// 臨時信箱
        message.setSubject("Test Email");
        message.setText("This is a test mail");
        mailSender.send(message);
    }

    @Test
    public void sendEmailWithTemplate() throws MessagingException {

        // Thymeleaf Context
        Context context =   new Context();
        context.setVariable("userName","Ray");

        String html = templateEngine.process("verification-email-template",context);

        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true,"utf-8");

        helper.setTo("vodobe5870@coderdir.com");
        helper.setSubject("認證信");
        helper.setText(html,true);
        helper.setFrom("會員系統<k9102188@gamil.com>");

        ClassPathResource logo = new ClassPathResource("static/images/verified.png");
        helper.addInline("verification_logo",logo);

        mailSender.send(mimeMailMessage);
    }

}
