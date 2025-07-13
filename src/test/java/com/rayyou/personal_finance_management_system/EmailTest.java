package com.rayyou.personal_finance_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("u5day@mechanicspedia.com");
        message.setSubject("Test Email");
        message.setText("This is a test mail");
        javaMailSender.send(message);
    }

}
