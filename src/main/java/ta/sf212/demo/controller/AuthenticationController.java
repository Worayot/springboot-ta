package ta.sf212.demo.controller;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import ta.sf212.demo.model.response.LoginResponse;
import ta.sf212.demo.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token (public)")
    public LoginResponse login(@RequestParam String username, @RequestParam String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            return new LoginResponse(401, "Invalid login credentials", null);
        }

        String token = jwtUtil.generateToken(username);

        // Check if the token is null and throw an exception
        if (token == null) {
            throw new RuntimeException("Failed to generate JWT token.");
        }

        return new LoginResponse(200, "Login successful", token);
    }
}