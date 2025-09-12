package com.note0.dto;

import java.time.LocalDateTime;

import com.note0.entity.UserRole;

public class UserProfileDTO {
    
    private String id;
    private String fullName;
    private String email;
    private String collegeName;
    private String branch;
    private Integer semester;
    private UserRole role;
    private LocalDateTime createdAt;
    
    // Constructors
    public UserProfileDTO() {}
    
    public UserProfileDTO(String id, String fullName, String email, String collegeName, 
                         String branch, Integer semester, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.collegeName = collegeName;
        this.branch = branch;
        this.semester = semester;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCollegeName() {
        return collegeName;
    }
    
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public Integer getSemester() {
        return semester;
    }
    
    public void setSemester(Integer semester) {
        this.semester = semester;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
