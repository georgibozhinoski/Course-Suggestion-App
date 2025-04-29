package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;

import java.util.Optional;

public interface StudyMajorService {
    StudyMajor findByMajorName(String majorName);
    StudyMajor findByMajorId(Long majorId);
    Long getStudyMajorIdByUserId(Long userId);
}
