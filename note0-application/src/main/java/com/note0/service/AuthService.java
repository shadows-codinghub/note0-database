package com.note0.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.note0.dto.AuthResponseDTO;
import com.note0.dto.UserLoginDTO;
import com.note0.dto.UserProfileDTO;
import com.note0.dto.UserRegistrationDTO;
import com.note0.entity.User;
import com.note0.entity.UserRole;
import com.note0.repository.UserRepository;
import com.note0.security.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public AuthResponseDTO register(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }
        
        User user = new User();
        user.setFullName(registrationDTO.getFullName());
        user.setEmail(registrationDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setCollegeName(registrationDTO.getCollegeName());
        user.setBranch(registrationDTO.getBranch());
        user.setSemester(registrationDTO.getSemester());
        user.setRole(UserRole.REGISTERED);
        
        user = userRepository.save(user);
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        
        String token = jwtUtil.generateToken(user, extraClaims);
        UserProfileDTO userProfile = userService.getUserProfile(user.getId());
        
        return new AuthResponseDTO("User registered successfully!", token, userProfile);
    }
    
    public AuthResponseDTO login(UserLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        
        String token = jwtUtil.generateToken(user, extraClaims);
        UserProfileDTO userProfile = userService.getUserProfile(user.getId());
        
        return new AuthResponseDTO("Login successful!", token, userProfile);
    }
}
