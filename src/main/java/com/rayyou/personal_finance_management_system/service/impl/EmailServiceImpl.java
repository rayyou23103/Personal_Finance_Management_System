package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.repository.UserRepository;
import com.rayyou.personal_finance_management_system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository){
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    // 寄送信箱認證信
    @Override
    public void sendVerificationEmail(String email) {

    }

    // 寄送密碼重設信
    @Override
    public void sendResetToken(String email) {

    }
}
