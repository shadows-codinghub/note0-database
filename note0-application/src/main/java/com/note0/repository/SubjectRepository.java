package com.note0.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.note0.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    
    Optional<Subject> findByName(String name);
    
    boolean existsByName(String name);
    
}
