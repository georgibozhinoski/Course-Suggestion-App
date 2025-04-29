package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyMajorRepository extends JpaRepository<StudyMajor, Long> {

    Optional<StudyMajor> findByMajorName(String majorName);
    Optional<StudyMajor> findByMajorId(Long majorId);
}
