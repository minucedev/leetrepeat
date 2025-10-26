package com.leetrepeat.backend.service;

import com.leetrepeat.backend.config.JwtService;
import com.leetrepeat.backend.dto.AuthRequest;
import com.leetrepeat.backend.dto.AuthResponse;
import com.leetrepeat.backend.entity.User;
import com.leetrepeat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    //Register
    public AuthResponse register(AuthRequest request) {
        //Check existed user
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        //Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userRepository.save(user);

        //Create token
        String token = jwtService.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setMessage("Register success!");
        return response;

    }

    //Login
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setMessage("Login success");
        return response;
    }


}
