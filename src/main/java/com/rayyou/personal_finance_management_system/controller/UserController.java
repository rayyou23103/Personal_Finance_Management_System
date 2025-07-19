package com.rayyou.personal_finance_management_system.controller;

import com.rayyou.personal_finance_management_system.dto.*;
import com.rayyou.personal_finance_management_system.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid UserRegisterDTO dto) {
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid UserLoginDTO dto) {
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
    public ResponseEntity<Map<String, Object>> resendVerification(@RequestBody @Valid ResendVerificationRequestDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.resendVerification(dto);
            response.put("success", true);
            log.info("重新寄送驗證信成功：{}", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.warn("重新寄送驗證信失敗：{}，原因：{}", dto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.error("重新寄送驗證信過程中發生錯誤：{}", dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/password/reset-request")
    public ResponseEntity<Map<String, Object>> resetRequest(@RequestBody ResetPasswordRequestDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.resetRequest(dto);
            response.put("success", true);
            log.info("密碼重設認證信已發送到{}", dto.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.warn("信箱尚未註冊:{}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            log.error("密碼重設請求處理過程錯誤：{}", dto.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @GetMapping("/password/reset-confirm")
    public void resetConfirm(@RequestParam String token, HttpServletResponse response) throws IOException {
        try {
            userService.resetConfirm(token);
            response.sendRedirect("/password-reset.html?token=" + token);
        } catch (IllegalArgumentException e) {
            response.sendRedirect("/password-reset.html?error=true");
        }

    }

    @PostMapping("/password/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(ResetPasswordDTO dto) {
        try {
            userService.resetPassword(dto);
            log.info("密碼已成功重設");
        } catch (IllegalArgumentException e) {
            log.warn("");
        }
        return ResponseEntity.ok().build();
    }
}
