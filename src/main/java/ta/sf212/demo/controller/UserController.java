package ta.sf212.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import ta.sf212.demo.model.User;
import ta.sf212.demo.model.request.UserRequest;
import ta.sf212.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user (public)")
    public User registerUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getName(),
                userRequest.getSurname()
        );
    }
}