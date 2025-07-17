package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.entity.User;
import com.rayyou.personal_finance_management_system.repository.UserRepository;
import com.rayyou.personal_finance_management_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public Integer register(String username,String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email 重複註冊");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username ,email, hashedPassword);
        user = userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(user -> passwordEncoder.matches(rawPassword, user.getPassword())).orElse(false);
    }

    @Override
    public Boolean verifyEmail(String token) {
        return null;
    }

    public void resendVerification(String email){}

    @Override
    public void resetRequest(String email) {

    }
    @Override
    public void resetConfirm(String token, String newPassword) {

    }

    // 寄送信箱認證信

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
            // Thymeleaf Context
            Context context = new Context();
            Map<String,Object> map = new HashMap<>();
            map.put("userName",user.getUsername());
            map.put("token",token);
            context.setVariables(map);

            templateEngine.process("verification-email-template",context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"utf-8");

            helper.setFrom("⟪信箱認證⟫會員系統");
            helper.setTo(user.getEmail());
            helper.setSubject("信箱驗證");

            ClassPathResource logo = new ClassPathResource("/static/images/verified.png");
            helper.addInline("verification_logo",logo);

            helper.setText("html",true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
