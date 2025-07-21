package com.rayyou.personal_finance_management_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig extends SecurityConfigurerAdapter {

    @Value("${app.security.enabled:true}") // 預設為 true，若未配置則啟用安全
    private boolean securityEnabled;



    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        UserDetails user1 = User
                .withUsername("user1")
                .password("{noop}111")
                .build();

        UserDetails user2 = User
                .withUsername("user2")
                .password("{noop}222")
                .build();

        UserDetails user3 = User
                .withUsername("user3")
                .password("{noop}333")
                .build();

        return new InMemoryUserDetailsManager(List.of(user1,user2,user3));
    }

}
