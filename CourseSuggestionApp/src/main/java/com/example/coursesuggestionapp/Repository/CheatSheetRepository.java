package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.CheatSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheatSheetRepository extends JpaRepository<CheatSheet, Long> {
    List<CheatSheet> findByCourseCourseId(Long courseId);

}
