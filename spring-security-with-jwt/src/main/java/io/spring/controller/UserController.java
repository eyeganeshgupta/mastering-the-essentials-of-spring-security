package io.spring.controller;

import io.spring.dto.LoginRequest;
import io.spring.dto.RegisterRequest;
import io.spring.response.ApiResponse;
import io.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody RegisterRequest registerRequest) {
        ApiResponse<?> response = userService.registerUser(registerRequest);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody LoginRequest loginRequest) {
        ApiResponse<?> response = userService.loginUser(loginRequest);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
}
