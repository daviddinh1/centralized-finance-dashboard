package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.LoginRequest;
import com.finance.dashboard.dto.request.RegisterRequest;
import com.finance.dashboard.dto.response.AuthResponse;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    //create a route for creating a user
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthResponse response = authService.register(registerRequest);

        //return status 201 for creating a user
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //create a route for logging in a user
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        AuthResponse response =  authService.login(loginRequest);

        //this returns 200
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));

        Map<String, String> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());

        return ResponseEntity.ok(response);
    }
}
