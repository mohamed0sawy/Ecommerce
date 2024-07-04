package com.academy.Ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(config ->
                        config
                                .requestMatchers("/api/v1/").permitAll()
                                .requestMatchers("/api/v1/forgetPassword").permitAll()
                                .requestMatchers("/api/v1/reset").permitAll()
                                .requestMatchers("/api/v1/login").permitAll()
                                .requestMatchers("/api/v1/register").permitAll()
                                .requestMatchers("/api/v1/activate").permitAll()
                                .requestMatchers("/api/v1/del").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login ->
                        login
                                .loginPage("/api/v1/login")
                                .loginProcessingUrl("/authenticateTheUser")
//                                .successHandler(customAuthenticationSuccessHandler)
//                                .failureHandler(customAuthenticationFailureHandler)
                                .permitAll())
                .logout(logout ->
                        logout
                                .permitAll()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/api/v1/login"));
        return http.build();
    }

}
