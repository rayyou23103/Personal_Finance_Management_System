package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.dto.*;
import com.rayyou.personal_finance_management_system.entity.User;
import com.rayyou.personal_finance_management_system.repository.UserRepository;
import com.rayyou.personal_finance_management_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public Integer register(UserRegisterDTO dto) {
        String email = dto.getEmail();
        String rawPassword = dto.getPassword();
        String username = dto.getUsername();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email 重複註冊");
        }

        // 加密
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, hashedPassword);
        user = userRepository.save(user);

        sendVerificationEmail(user);

        return user.getUserId();
    }

    @Override
    public boolean login(UserLoginDTO dto) {
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            log.warn("登入失敗，信箱尚未註冊:{}", dto.getEmail());
            throw new IllegalArgumentException("此信箱尚未註冊");
        }
        return userOpt.map(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword())).orElse(false);
    }

    @Override
    public Boolean verifyEmail(String token) {
        Optional<User> userOpt = userRepository.findByEmailVerificationToken(token);

        if (userOpt.isEmpty()) {
            log.warn("驗證失敗，查無對應 token");
            return false;
        }

        User user = userOpt.get();
        LocalDateTime tokenExpiredAt = user.getEmailTokenExpiredAt();

        if (tokenExpiredAt == null || tokenExpiredAt.isBefore(LocalDateTime.now())) {
            log.warn("驗證失敗：token 過期，使用者{}" + user.getEmail());
            throw new IllegalArgumentException("Token 過期");
        }

        user.clearEmailVerificationToken();
        userRepository.save(user);
        log.info("驗證成功：{}", user.getEmail());

        return true;
    }

    @Override
    public void resendVerification(ResendVerificationRequestDTO dto) {
        String email = dto.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.warn("查無信箱:{}", email);
            throw new IllegalArgumentException("該信箱尚未註冊");
        }

        User user = userOpt.get();
        LocalDateTime tokenExpiredAt = user.getEmailTokenExpiredAt();

        if (tokenExpiredAt != null || tokenExpiredAt.isAfter(LocalDateTime.now())) {
            log.warn("驗證信尚未過期，不重複發送:{}", email);
            throw new IllegalArgumentException("驗證信尚未過期，請稍後再試");
        }

        if (user.getEmailVerified()) {
            log.warn("該 Email 已驗證:{}", user.getEmail());
            throw new IllegalArgumentException("信箱已完成驗證");
        }

        sendVerificationEmail(user);

    }

    @Override
    public void resetRequest(ResetPasswordRequestDTO dto) {
        String email = dto.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.warn("密碼重設請求失敗，信箱尚未註冊{}", email);
            throw new IllegalArgumentException("該信箱尚未註冊");
        }

        User user = userOpt.get();
        sendPasswordResetEmail(user);
        log.info("認證信已寄出{}", email);
    }

    @Override
    public boolean resetConfirm(String token) {
        Optional<User> userOpt = userRepository.findByEmailVerificationToken(token);

        if (userOpt.isEmpty()) {
            log.warn("驗證失敗，查無對應 token");
            throw new IllegalArgumentException("驗證失敗，查無對應 token");
        }

        User user = userOpt.get();
        LocalDateTime tokenExpiredAt = user.getEmailTokenExpiredAt();

        if (tokenExpiredAt == null || tokenExpiredAt.isBefore(LocalDateTime.now())) {
            log.warn("驗證失敗：token 過期，使用者{}" + user.getEmail());
            throw new IllegalArgumentException("Token 過期");
        }

        return true;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDTO dto) {
        User user = userRepository.findByPasswordResetToken(dto.getToken()).orElseThrow(() -> new IllegalArgumentException("無效的重設密碼連結"));
        LocalDateTime passwordResetTokenExpiredAt = user.getPasswordResetTokenExpiredAt();
        if ( passwordResetTokenExpiredAt== null || passwordResetTokenExpiredAt.isAfter(LocalDateTime.now())){
            throw new IllegalArgumentException("Token 已過期，請重新申請重設密碼");
        }

        String rawPassword = dto.getNewPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        user.clearPasswordResetToken();

        userRepository.save(user);
        log.info("用戶重設密碼成功{}",user.getEmail());
    }

    // 寄送信箱認證信
    private void sendVerificationEmail(User user) {
        String email = user.getEmail();

        // 產生驗證 token, token 到期時間
        String token = UUID.randomUUID().toString();
        user.applyVerificationToken(token, LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        // 寄送信件
        try {
            // Thymeleaf Context
            Context context = new Context();
            Map<String, Object> map = new HashMap<>();
            map.put("userName", user.getUsername());
            map.put("token", token);
            context.setVariables(map);

            templateEngine.process("verification-email-template", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // MimeMessage資訊設定
            helper.setFrom("⟪信箱認證⟫會員系統");
            helper.setTo(user.getEmail());
            helper.setSubject("信箱驗證");

            ClassPathResource logo = new ClassPathResource("/static/images/verified.png");
            helper.addInline("verification_logo", logo);

            helper.setText("html", true);

            mailSender.send(mimeMessage);
            log.info("認證信已發送到{}", email);
        } catch (MessagingException e) {
            log.error("認證信寄送失敗:{}", email, e);
            throw new RuntimeException("寄送失敗");
        }
    }

    private void sendPasswordResetEmail(User user) {
        String email = user.getEmail();

        //
        String token = UUID.randomUUID().toString();
        user.applyVerificationToken(token, LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        try {
            // Thymeleaf Context
            Context context = new Context();
            Map<String, Object> variablesMap = new HashMap<>();
            variablesMap.put("username", user.getUsername());
            variablesMap.put("token", token);
            context.setVariables(variablesMap);
            String html = templateEngine.process("reset-password-email-template", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(user.getEmail());
            helper.setSubject("重設密碼連結");
            helper.setText(html, true);

            ClassPathResource logo = new ClassPathResource("/static/images/verified.png");
            helper.addInline("verification_logo", logo);

            mailSender.send(mimeMessage);
            log.info("密碼重設信已發送到:{}", email);

        } catch (MessagingException e) {
            log.error("密碼重設信寄送失敗:{}", email, e);
            throw new RuntimeException("寄送失敗");
        }


    }
}
