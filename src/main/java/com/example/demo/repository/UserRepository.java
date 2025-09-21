package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);
}

