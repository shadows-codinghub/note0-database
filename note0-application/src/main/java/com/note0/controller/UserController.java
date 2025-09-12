package com.note0.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.note0.dto.UserProfileDTO;
import com.note0.entity.User;
import com.note0.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            UserProfileDTO profile = userService.getUserProfile(user.getId());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("Server error"));
        }
    }
    
    // Error response class
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
