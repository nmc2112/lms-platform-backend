//package com.example.demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(@Lazy UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .requestMatchers("/user/login").permitAll()  // Permit access to H2 console
//                .anyRequest().authenticated()
//                .and()
//                .formLogin(formLogin -> formLogin
////                        .loginPage("/user/login")  // Login page URL
//                        .permitAll()                 // Allow all users to access login page
////                        .successForwardUrl("/user/get-all-users")  // URL on successful login
//                        .failureForwardUrl("/api/login/failure"));
////                .loginProcessingUrl("/api/login")
////                .successForwardUrl("/api/login/success")
////                .failureForwardUrl("/api/login/failure")
////                .permitAll();
//
//        // For accessing H2 console
////        http.headers().frameOptions().disable();
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
