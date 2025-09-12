package com.note0.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.note0.dto.MaterialDTO;
import com.note0.dto.RatingDTO;
import com.note0.entity.Material;
import com.note0.entity.MaterialStatus;
import com.note0.entity.Rating;
import com.note0.entity.Subject;
import com.note0.entity.User;
import com.note0.repository.MaterialRepository;
import com.note0.repository.RatingRepository;
import com.note0.repository.SubjectRepository;

@Service
public class MaterialService {
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    public MaterialDTO uploadMaterial(String title, String description, String subjectId, 
                                    Integer moduleNumber, MultipartFile file, User uploader) {
        
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        // Save file
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);
        
        try {
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + fileName, e);
        }
        
        // Get file extension
        String fileType = "";
        if (file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")) {
            fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        }
        
        // Create material
        Material material = new Material();
        material.setTitle(title);
        material.setDescription(description);
        material.setFileType(fileType);
        material.setFilePath(targetLocation.toString());
        material.setModuleNumber(moduleNumber);
        material.setUploader(uploader);
        material.setSubject(subject);
        material.setStatus(MaterialStatus.PENDING);
        
        Material savedMaterial = materialRepository.save(material);
        
        return convertToDTO(savedMaterial);
    }
    
    public List<MaterialDTO> getAllApprovedMaterials() {
        List<Material> materials = materialRepository.findApprovedMaterialsWithDetails(MaterialStatus.APPROVED);
        return materials.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RatingDTO rateMaterial(String materialId, RatingDTO ratingDTO, User user) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        
        // Check if user already rated this material
        Rating existingRating = ratingRepository.findByMaterialIdAndUserId(materialId, user.getId()).orElse(null);
        
        Rating rating;
        if (existingRating != null) {
            // Update existing rating
            existingRating.setScore(ratingDTO.getScore());
            rating = ratingRepository.save(existingRating);
        } else {
            // Create new rating
            rating = new Rating();
            rating.setScore(ratingDTO.getScore());
            rating.setMaterial(material);
            rating.setUser(user);
            rating = ratingRepository.save(rating);
        }
        
        // Update average rating
        updateAverageRating(materialId);
        
        return new RatingDTO(rating.getScore());
    }
    
    private void updateAverageRating(String materialId) {
        Double avgRating = ratingRepository.findAverageRatingByMaterialId(materialId);
        if (avgRating != null) {
            Material material = materialRepository.findById(materialId).orElse(null);
            if (material != null) {
                material.setAvgRating(Math.round(avgRating * 100.0) / 100.0); // Round to 2 decimal places
                materialRepository.save(material);
            }
        }
    }
    
    private MaterialDTO convertToDTO(Material material) {
        return new MaterialDTO(
                material.getId(),
                material.getTitle(),
                material.getDescription(),
                material.getFileType(),
                material.getFilePath(),
                material.getModuleNumber(),
                material.getAvgRating(),
                material.getUploadDate(),
                material.getStatus(),
                material.getUploader().getFullName(),
                material.getSubject().getName(),
                material.getSubject().getId()
        );
    }
}
