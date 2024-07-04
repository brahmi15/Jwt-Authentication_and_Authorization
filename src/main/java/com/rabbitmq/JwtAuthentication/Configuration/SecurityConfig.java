package com.rabbitmq.JwtAuthentication.Configuration;

import com.rabbitmq.JwtAuthentication.Security.JwtAuthenticationEntryPoint;
import com.rabbitmq.JwtAuthentication.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //enables method-level security
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()) //Cross-Site Request Forgery
                .cors(cors -> cors.disable()) //Cross-Origin Resource Sharing
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login").permitAll().anyRequest().authenticated()) //"/auth/login" URL/endpoint is accessible to everyone
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //application will not create or use HTTP sessions

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); //ensures that JWT processing occurs before username-password authentication
        return http.build();
    }
}
