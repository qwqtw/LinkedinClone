package com.example.linkedinclone.securingweb;

import com.example.linkedinclone.handler.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/index", "/register", "/home", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin") // Restrict admin routes
                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("username")
                        .defaultSuccessUrl("/home", true) // Redirect to home after login
                        .failureHandler(customAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
