package com.example.demo.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.response.ResponseDto;
import com.example.demo.model.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseDto<List<User>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseDto<Optional<User>> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/upsert")
    public ResponseDto<User> upsert(@RequestBody User user) {
        return service.upsert(user);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<String> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}