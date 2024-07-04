package com.rabbitmq.JwtAuthentication.Services;

import com.rabbitmq.JwtAuthentication.Interfaces.UserRepository;
import com.rabbitmq.JwtAuthentication.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
            return userRepository.findAll();
    }

    public Optional<User> getDetailById(int id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }


    public void deleteDetail(int id) {
        userRepository.deleteById(id);
    }


    public  boolean exists(int id) {
        return userRepository.existsById(id);
    }


    public User updateDetail(User user, int id) {
        user.setId(id);
        return userRepository.save(user);
    }
}



