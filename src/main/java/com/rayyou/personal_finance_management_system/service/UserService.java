package com.rayyou.personal_finance_management_system.service;

import org.springframework.stereotype.Service;

public interface UserService {
    boolean login(String email, String password);

    Integer register(String username,   String email, String password);
}
