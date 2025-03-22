package io.spring.service;

import io.spring.dto.LoginRequest;
import io.spring.dto.RegisterRequest;
import io.spring.entity.User;
import io.spring.repository.UserRepository;
import io.spring.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<User> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ApiResponse<>(false, "Username already exists", null);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse<>(false, "Email already in use", null);
        }

        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRegistrationDate(LocalDateTime.now());

        userRepository.save(user);

        return new ApiResponse<>(true, "User registered successfully", user);
    }

    public ApiResponse<User> loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return new ApiResponse<>(false, "Invalid username or password", null);
        }

        User user = userOptional.get();
        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            return new ApiResponse<>(false, "Invalid username or password", null);
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return new ApiResponse<>(true, "Login successful", user);
    }
}
