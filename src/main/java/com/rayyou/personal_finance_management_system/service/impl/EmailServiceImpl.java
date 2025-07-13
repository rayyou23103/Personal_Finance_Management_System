package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.entity.User;
import com.rayyou.personal_finance_management_system.repository.UserRepository;
import com.rayyou.personal_finance_management_system.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    // 寄送信箱認證信
    @Override
    public void sendVerificationEmail(String email) {

        //查找註冊信箱
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("信箱：" + email + "尚未註冊");
        }

        User user = userOpt.get();

        // 產生驗證token
        String token = UUID.randomUUID().toString();
        user.applyVerificationToken(token, LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);

        // 寄送信件
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"utf-8");

            helper.setTo(user.getEmail());
            helper.setSubject("信箱驗證");
            helper.setFrom("⟪信箱認證⟫會員系統");

            Multipart mimeMultipart = new MimeMultipart();
            mimeMessage.setContent(mimeMultipart);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 寄送密碼重設信
    @Override
    public void sendResetToken(String email) {

    }
    private SimpleMailMessage setMailMessage(Collection<String> receivers, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receivers.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("會員系統通知");

        return message;
    }

    private void sendMimeMail(String email){

    }


}
