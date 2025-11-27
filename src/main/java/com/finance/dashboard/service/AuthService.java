package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.LoginRequest;
import com.finance.dashboard.dto.request.RegisterRequest;
import com.finance.dashboard.dto.response.AuthResponse;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest){
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());

        //create the user to send to the model
        User user = new User();
        user.setName(registerRequest.getName());
        user.setPassword(hashedPassword);
        user.setEmail(registerRequest.getEmail());

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateJWTToken(savedUser.getEmail());

        return new AuthResponse(token, savedUser.getEmail(),savedUser.getName());
    }

    public AuthResponse login(LoginRequest loginRequest){
        //Authenticate credentials
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        //Generate token if successful
        String token = jwtUtil.generateJWTToken(loginRequest.getEmail());
        //fetch user and return auth response
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(token, user.getEmail(),user.getName());

    }


}
