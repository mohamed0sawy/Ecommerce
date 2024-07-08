package com.academy.Ecommerce.config;

import com.academy.Ecommerce.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationSuccessHandler customAuthenticationSuccessHandler,
                                           AuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception{
        http.authorizeHttpRequests(config ->
                config
                        .requestMatchers("/api/v1/").permitAll()
                        .requestMatchers("/api/v1/forgetPassword").permitAll()
                        .requestMatchers("/api/v1/reset").permitAll()
                        .requestMatchers("/api/v1/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(login ->
                        login
                                .loginPage("/api/v1/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler)
                                .permitAll())
                .logout(logout ->
                        logout
                                .permitAll()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/api/v1/login"));
        return http.build();
    }
}
