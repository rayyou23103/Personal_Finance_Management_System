package com.rayyou.personal_finance_management_system.controller;

import com.rayyou.personal_finance_management_system.dto.UserLoginDTO;
import com.rayyou.personal_finance_management_system.dto.UserRegisterDTO;
import com.rayyou.personal_finance_management_system.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = userService.register(dto.getUsername(), dto.getEmail(), dto.getPassword());
            response.put("success", true);
            response.put("userId", userId);
            log.info("新用戶註冊：{}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.warn("註冊失敗，{}重複註冊", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Internal server error");
            log.error("系統錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        Boolean success = userService.login(dto.getEmail(), dto.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        if (success) {
            log.info("登入成功：{}", dto.getEmail());
            return ResponseEntity.ok(response);
        } else {
            log.warn("登入失敗：{}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/email/verify")
    public void verifyEmail(@RequestParam String token, HttpServletResponse response) throws IOException {
        try {
            userService.verifyEmail(token);
            log.info("信箱驗證成功");
            response.sendRedirect("/verify-success.html");
        } catch (IllegalArgumentException e) {
            log.warn("信箱驗證失敗：", e.getMessage());
            response.sendRedirect("/verify-fail.html");
        }
    }

    @PostMapping("/email/resend-verification")
    public ResponseEntity<Map<String, Object>> resendVerification(@RequestBody String email) {
        userService.resendVerification(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset-request")
    public ResponseEntity<Map<String, Object>> resetRequest(@RequestBody String email) {
        userService.resetRequest(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset-confirm")
    public ResponseEntity<Map<String, Object>> resetConfirm(@RequestBody String token, @RequestBody String newPassword) {
        userService.resetConfirm(token, newPassword);
        return ResponseEntity.ok().build();
    }

}
