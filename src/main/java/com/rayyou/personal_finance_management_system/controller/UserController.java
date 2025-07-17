package com.rayyou.personal_finance_management_system.controller;

import com.rayyou.personal_finance_management_system.dto.UserLoginDTO;
import com.rayyou.personal_finance_management_system.dto.UserRegisterDTO;
import com.rayyou.personal_finance_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        Boolean success = userService.login(dto.getEmail(), dto.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/email/verify")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/resend-verification")
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
        userService.resetConfirm(token,newPassword);
        return ResponseEntity.ok().build();
    }

}
