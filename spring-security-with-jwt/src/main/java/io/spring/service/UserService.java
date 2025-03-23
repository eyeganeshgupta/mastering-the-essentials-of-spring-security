package io.spring.service;

import io.spring.dto.LoginRequest;
import io.spring.dto.RegisterRequest;
import io.spring.entity.User;
import io.spring.repository.UserRepository;
import io.spring.response.ApiResponse;
import io.spring.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

    public ApiResponse<String> loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return new ApiResponse<>(false, "Invalid username or password", null);
        }

        User user = userOptional.get();

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        try {
            // Authenticate using the AuthenticationManager
            Authentication result = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(result);

            if (result.isAuthenticated()) {
                System.out.println("Inside isAuthenticated");
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return new ApiResponse<>(true, "Successfully logged in", jwtUtil.generateToken(user));
            } else {
                return new ApiResponse<>(false, "Invalid username or password", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }
}
