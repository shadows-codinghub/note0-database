package com.note0.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.note0.entity.Material;
import com.note0.entity.MaterialStatus;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    
    List<Material> findByStatusOrderByUploadDateDesc(MaterialStatus status);
    
    List<Material> findBySubjectIdAndStatusOrderByUploadDateDesc(String subjectId, MaterialStatus status);
    
    List<Material> findByUploaderIdOrderByUploadDateDesc(String uploaderId);
    
    @Query("SELECT m FROM Material m " +
           "JOIN FETCH m.subject s " +
           "JOIN FETCH m.uploader u " +
           "WHERE m.status = :status " +
           "ORDER BY m.uploadDate DESC")
    List<Material> findApprovedMaterialsWithDetails(@Param("status") MaterialStatus status);
    
}
