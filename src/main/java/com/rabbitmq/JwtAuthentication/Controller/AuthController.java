package com.rabbitmq.JwtAuthentication.Controller;

import com.rabbitmq.JwtAuthentication.Models.JwtRequest;
import com.rabbitmq.JwtAuthentication.Models.JwtResponse;
import com.rabbitmq.JwtAuthentication.Security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController // this class handles incoming HTTP requests and produces appropriate HTTP responses
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService; //bean in config file

    @Autowired
    private AuthenticationManager manager; //bean in config file


    @Autowired
    private JwtHelper helper;

    //https://localhost:8080/auth/login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) { //ResponseEntity represents the whole HTTP response including status code, headers, and body
        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }
}
