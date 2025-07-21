package com.rayyou.personal_finance_management_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rayyou.personal_finance_management_system.dto.UserLoginDTO;
import com.rayyou.personal_finance_management_system.dto.UserRegisterDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void register() throws Exception {
        // Arrange
        UserRegisterDTO dto =new UserRegisterDTO("testUser","password123","test@gmail.com");

        String json = objectMapper.writeValueAsString(dto);

        // Act
        RequestBuilder requestBuilder  = MockMvcRequestBuilders
                .post("/users/register")
                .contentType("application/json")
                .content(json);

        // Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void login() throws Exception{
        // Arrange
        UserLoginDTO dto = new UserLoginDTO("test@gmail.com","wrongPassword");

        String json = objectMapper.writeValueAsString(dto);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType("application/json")
                .content(json);

        // Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void loginWithWrongPassword() throws Exception{
        // Arrange
        UserLoginDTO dto = new UserLoginDTO("test@gmail.com","password123");

        String json = objectMapper.writeValueAsString(dto);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType("application/json")
                .content(json);

        // Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));

    }


    @Test
    void verifyEmail() throws Exception {
        // Arrange
        String token = "valid-token";

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/email/verify")
                .param("token",token);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("verify-success.html"));

    }

    @Test
    void verifyEmailWithInvalidToken() throws Exception {
        String invalidToken = "invalid-token";

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/email/verify")
                                .param("token", invalidToken)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/verify-fail.html"));
    }

    @Test
    void resendVerification() {
    }

    @Test
    void resetRequest() {
    }

    @Test
    void resetConfirm() {
    }

    @Test
    void resetPassword() {
    }
}