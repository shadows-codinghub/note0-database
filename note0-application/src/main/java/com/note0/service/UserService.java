package com.note0.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.note0.dto.UserProfileDTO;
import com.note0.entity.User;
import com.note0.entity.UserRole;
import com.note0.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    
    
    public UserProfileDTO getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserProfileDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCollegeName(),
                user.getBranch(),
                user.getSemester(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
    
    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserProfileDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCollegeName(),
                user.getBranch(),
                user.getSemester(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
    
    public User verifyUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setRole(UserRole.VERIFIED);
        return userRepository.save(user);
    }
    
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
}
