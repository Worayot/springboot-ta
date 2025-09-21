package com.example.demo.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User upsert(User user) {
        return repo.findById(user.getId())
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setSurname(user.getSurname());
                    existing.setAge(user.getAge());
                    return repo.save(existing);
                })
                .orElseGet(() -> repo.save(user));
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public void deleteById(String id) {
        if (!repo.existsById(id)) throw new RuntimeException("User not found");
        repo.deleteById(id);
    }
}