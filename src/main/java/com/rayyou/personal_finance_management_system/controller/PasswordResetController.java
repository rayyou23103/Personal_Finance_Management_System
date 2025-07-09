package com.rayyou.personal_finance_management_system.controller;

import com.rayyou.personal_finance_management_system.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/resetpwd")
public class PasswordResetController {


    private PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService){
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/sendtoken")
    public String sendResetToken(String email){
        return "回傳token";
    }

    @PostMapping("/confirmtoken")
    public ResponseEntity<String> resetPassword(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}