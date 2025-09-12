package com.note0.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.note0.dto.UserProfileDTO;
import com.note0.entity.User;
import com.note0.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            // This would need to be implemented in UserService
            // For now, returning a placeholder response
            return ResponseEntity.ok(new MessageResponse("Admin endpoint - users list"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server error while fetching users"));
        }
    }
    
    @PutMapping("/users/{id}/verify")
    public ResponseEntity<?> verifyUser(@PathVariable String id) {
        try {
            User user = userService.verifyUser(id);
            UserProfileDTO userProfile = userService.getUserProfile(user.getId());
            return ResponseEntity.ok(new VerifyResponse("User has been successfully verified.", userProfile));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server error while verifying user"));
        }
    }
    
    // Response classes
    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    public static class VerifyResponse {
        private String message;
        private UserProfileDTO user;
        
        public VerifyResponse(String message, UserProfileDTO user) {
            this.message = message;
            this.user = user;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public UserProfileDTO getUser() {
            return user;
        }
        
        public void setUser(UserProfileDTO user) {
            this.user = user;
        }
    }
    
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
