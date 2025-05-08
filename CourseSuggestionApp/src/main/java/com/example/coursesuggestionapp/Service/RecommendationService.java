package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.RecommendedCourseDTO;

import java.util.List;

public interface RecommendationService {
    public List<RecommendedCourseDTO> getRecommendedCoursesForUser(Long userId, String semesterType, String level, String reason);
    public List<RecommendedCourseDTO> getRecommendedCoursesForCourseList(List<Long> courseIds);
    public List<RecommendedCourseDTO> getRecommendedCoursesForCourseListByCode(List<String> codes);
    public void reloadRecommender();
}
