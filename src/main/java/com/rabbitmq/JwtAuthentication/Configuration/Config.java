package com.rabbitmq.JwtAuthentication.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class Config {
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder() //User.builder a static method that creates object of UserDetails
                .username("BRAHMI")
                .password(passwordEncoder().encode("brahmi")) //uses BCrypt hashing function/algorithm
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("MANSI")
                .password(passwordEncoder().encode("mansi"))
                .roles("USER")
                .build();

        UserDetails anotherParty = User.builder()
                .username("PULKIT")
                .password(passwordEncoder().encode("pulkit"))
                .roles("Another_Party")
                .build();

        return new InMemoryUserDetailsManager(admin, user, anotherParty);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception { //provides a way to access Authentication Manager
        return builder.getAuthenticationManager();
    }
    //AuthenticationManager - central interface, responsible for processing authentication requests.
}
