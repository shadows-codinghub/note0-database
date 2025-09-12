package com.note0.dto;

import java.time.LocalDateTime;

import com.note0.entity.MaterialStatus;

public class MaterialDTO {
    
    private String id;
    private String title;
    private String description;
    private String fileType;
    private String filePath;
    private Integer moduleNumber;
    private Double avgRating;
    private LocalDateTime uploadDate;
    private MaterialStatus status;
    private String uploaderName;
    private String subjectName;
    private String subjectId;
    
    // Constructors
    public MaterialDTO() {}
    
    public MaterialDTO(String id, String title, String description, String fileType, 
                      String filePath, Integer moduleNumber, Double avgRating, 
                      LocalDateTime uploadDate, MaterialStatus status, 
                      String uploaderName, String subjectName, String subjectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.filePath = filePath;
        this.moduleNumber = moduleNumber;
        this.avgRating = avgRating;
        this.uploadDate = uploadDate;
        this.status = status;
        this.uploaderName = uploaderName;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Integer getModuleNumber() {
        return moduleNumber;
    }
    
    public void setModuleNumber(Integer moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    public Double getAvgRating() {
        return avgRating;
    }
    
    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public MaterialStatus getStatus() {
        return status;
    }
    
    public void setStatus(MaterialStatus status) {
        this.status = status;
    }
    
    public String getUploaderName() {
        return uploaderName;
    }
    
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public String getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
