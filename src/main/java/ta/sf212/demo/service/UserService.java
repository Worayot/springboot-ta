package ta.sf212.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ta.sf212.demo.model.User;
import ta.sf212.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection (recommended)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String rawPassword, String name, String surname) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user;
        user = new User(username, encodedPassword, name, surname);
        return userRepository.save(user);
    }
}
