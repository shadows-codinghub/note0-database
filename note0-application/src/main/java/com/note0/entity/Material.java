package com.note0.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "materials")
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 255)
    @Column(nullable = false)
    private String title;
    
    @Size(max = 2000)
    private String description;
    
    @Size(max = 50)
    @Column(name = "file_type")
    private String fileType;
    
    @Size(max = 500)
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "module_number")
    private Integer moduleNumber;
    
    @Column(name = "avg_rating")
    private Double avgRating = 0.0;
    
    @Column(name = "upload_date", nullable = false, updatable = false)
    private LocalDateTime uploadDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialStatus status = MaterialStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    @NotNull
    private User uploader;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @NotNull
    private Subject subject;
    
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings;
    
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
    
    // Constructors
    public Material() {}
    
    public Material(String title, String description, String fileType, String filePath, 
                   Integer moduleNumber, User uploader, Subject subject) {
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.filePath = filePath;
        this.moduleNumber = moduleNumber;
        this.uploader = uploader;
        this.subject = subject;
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
    
    public User getUploader() {
        return uploader;
    }
    
    public void setUploader(User uploader) {
        this.uploader = uploader;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    
    public List<Rating> getRatings() {
        return ratings;
    }
    
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
