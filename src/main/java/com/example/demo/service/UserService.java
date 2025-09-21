package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.response.HeaderDto;
import com.example.demo.model.dto.response.ResponseDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // Create a new user or update an existing user.
    public ResponseDto<User> upsert(User user) {
        User savedUser;
        HeaderDto header = new HeaderDto();

        if (user.getId() == null || !repo.existsById(user.getId())) {
            // New user
            savedUser = new User(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getAge()
            );
            repo.save(savedUser);
            header.setCode(201);
            header.setMessage("Added a new user.");
        } else {
            // Existing user, update
            Optional<User> existingOpt = repo.findById(user.getId());
            if (existingOpt.isPresent()) {
                User existing = existingOpt.get();
                existing.setName(user.getName());
                existing.setSurname(user.getSurname());
                existing.setAge(user.getAge());
                savedUser = repo.save(existing);
                header.setCode(200);
                header.setMessage("Updated existing user.");
            } else {
                throw new RuntimeException("User not found for update");
            }
        }

        ResponseDto<User> response = new ResponseDto<>();
        response.setHeader(header);
        response.setData(savedUser); // directly assign the entity
        return response;
    }

    // Retrieve all users.
    public ResponseDto<Optional<User>> getById(String id) {

        
        Optional<User> user = repo.findById(id);
        HeaderDto header = new HeaderDto();
        header.setCode(200);

        ResponseDto<Optional<User>> response = new ResponseDto<>();
        response.setHeader(header);
        response.setData(user); 

        if (!repo.existsById(id)) {
            header.setCode(404);
            header.setMessage("User not found");
        } else {
            repo.deleteById(id);
            header.setCode(200);
            header.setMessage("Retrieved user with id of " + id + ".");
        }

        return response;
    }

    // Retrieve all users.
    public ResponseDto<List<User>> getAll() {
        List<User> users = repo.findAll();
        HeaderDto header = new HeaderDto();
        header.setCode(200);
        header.setMessage("Retrieved all users.");

        ResponseDto<List<User>> response = new ResponseDto<>();
        response.setHeader(header);
        response.setData(users); // directly assign the list
        return response;
    }

    // Delete a user by ID.
    public ResponseDto<String> deleteById(String id) {
        HeaderDto header = new HeaderDto();
        String message;

        if (!repo.existsById(id)) {
            header.setCode(404);
            header.setMessage("User not found");
            message = "User with ID " + id + " not found.";
        } else {
            repo.deleteById(id);
            header.setCode(200);
            header.setMessage("User deleted successfully");
            message = "User with ID " + id + " deleted successfully.";
        }

        ResponseDto<String> response = new ResponseDto<>();
        response.setHeader(header);
        response.setData(message); // directly assign the message
        return response;
    }
}
