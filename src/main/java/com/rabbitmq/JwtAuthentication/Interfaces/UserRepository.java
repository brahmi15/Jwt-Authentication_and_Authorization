package com.rabbitmq.JwtAuthentication.Interfaces;

import com.rabbitmq.JwtAuthentication.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> /*<entity,entity's primary key data type>*/ {
    Optional<User> findById(int id); // Optional - handles cases where the result might be null.
}

