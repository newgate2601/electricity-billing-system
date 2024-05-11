package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.mapper.UserMapper;
import com.example.electricitybillingsystem.model.User;
import com.example.electricitybillingsystem.repository.UserRepository;
import com.example.electricitybillingsystem.vo.request.RegisterRequest;
import com.example.electricitybillingsystem.vo.response.AuthResponse;
import com.example.electricitybillingsystem.vo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    public AuthResponse login(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(user -> AuthResponse.builder()
                .user(userMapper.toUserResponse(user))
                .token(jwtService.generateToken(user))
                .build()).orElse(null);
    }

    public UserResponse saveUser(RegisterRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

}
