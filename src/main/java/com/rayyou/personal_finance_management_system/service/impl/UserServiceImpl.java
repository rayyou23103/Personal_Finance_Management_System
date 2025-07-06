package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.entity.User;
import com.rayyou.personal_finance_management_system.repository.UserRepository;
import com.rayyou.personal_finance_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(user -> passwordEncoder.matches(rawPassword, user.getPassword())).orElse(false);
    }

    @Override
    public Integer register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashedPassword);
        user =userRepository.save(user);
        return user.getUserId();
    }
}
