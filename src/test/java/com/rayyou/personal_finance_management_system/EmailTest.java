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

import java.util.UUID;

@SpringBootTest
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("nofetap643@dosonex.com");// 臨時信箱
        message.setSubject("Test Email");
        message.setText("This is a test mail");
        mailSender.send(message);
    }

    @Test
    public void sendEmailWithTemplate() throws MessagingException {
        // 產生驗證 token, token 到期時間
        String token = UUID.randomUUID().toString();

        // Thymeleaf Context
        Context context =   new Context();
        context.setVariable("userName","Ray");
        context.setVariable("token",token);

        String html = templateEngine.process("verification-email-template",context);

        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true,"utf-8");

        helper.setTo("nofetap643@dosonex.com");
        helper.setSubject("認證信");
        helper.setText(html,true);
        helper.setFrom("會員系統<k9102188@gamil.com>");

        ClassPathResource logo = new ClassPathResource("templates/images/verification_logo.png");
        helper.addInline("verification_logo",logo);

        mailSender.send(mimeMailMessage);
    }

}
