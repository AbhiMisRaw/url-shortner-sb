package com.abhi.shortner.service;

import com.abhi.shortner.dtos.LoginRequest;
import com.abhi.shortner.models.User;
import com.abhi.shortner.repository.UserRepository;
import com.abhi.shortner.security.jwt.JwtAuthenticationResponse;
import com.abhi.shortner.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  userRepository.save(user);
    }

    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // updating security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl)  authentication.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetails);

        return new JwtAuthenticationResponse(jwtToken);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("Username not found with username : " + name)
        );

    }
}
