package com.note0.dto;

public class AuthResponseDTO {
    
    private String message;
    private String token;
    private UserProfileDTO user;
    
    // Constructors
    public AuthResponseDTO() {}
    
    public AuthResponseDTO(String message, String token, UserProfileDTO user) {
        this.message = message;
        this.token = token;
        this.user = user;
    }
    
    public AuthResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserProfileDTO getUser() {
        return user;
    }
    
    public void setUser(UserProfileDTO user) {
        this.user = user;
    }
}
