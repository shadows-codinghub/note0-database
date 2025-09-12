package com.note0.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.note0.dto.MaterialDTO;
import com.note0.dto.RatingDTO;
import com.note0.entity.User;
import com.note0.service.MaterialService;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin(origins = "*")
public class MaterialController {
    
    @Autowired
    private MaterialService materialService;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("subjectId") String subjectId,
            @RequestParam(value = "moduleNumber", required = false) Integer moduleNumber,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        
        try {
            User user = (User) authentication.getPrincipal();
            MaterialDTO material = materialService.uploadMaterial(title, description, subjectId, moduleNumber, file, user);
            return ResponseEntity.status(201).body(new UploadResponse("File uploaded and record created successfully!", material));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server error while creating material record"));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllMaterials() {
        try {
            List<MaterialDTO> materials = materialService.getAllApprovedMaterials();
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server error while fetching materials"));
        }
    }
    
    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateMaterial(@PathVariable String id, @RequestBody RatingDTO ratingDTO, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            RatingDTO rating = materialService.rateMaterial(id, ratingDTO, user);
            return ResponseEntity.status(201).body(new RatingResponse("Thank you for your rating!", rating));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body(new ErrorResponse("Material not found"));
            }
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server error while submitting rating"));
        }
    }
    
    // Response classes
    public static class UploadResponse {
        private String message;
        private MaterialDTO material;
        
        public UploadResponse(String message, MaterialDTO material) {
            this.message = message;
            this.material = material;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public MaterialDTO getMaterial() {
            return material;
        }
        
        public void setMaterial(MaterialDTO material) {
            this.material = material;
        }
    }
    
    public static class RatingResponse {
        private String message;
        private RatingDTO rating;
        
        public RatingResponse(String message, RatingDTO rating) {
            this.message = message;
            this.rating = rating;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public RatingDTO getRating() {
            return rating;
        }
        
        public void setRating(RatingDTO rating) {
            this.rating = rating;
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
