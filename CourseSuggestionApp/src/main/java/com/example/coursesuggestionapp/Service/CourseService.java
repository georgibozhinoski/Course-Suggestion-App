package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.DTO.PassedCourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getCoursesByMajorIdAndSemesterNo(Long majorId, Integer semesterNo);
    List<CourseDTO> getElectiveCoursesByMajorIdAndLevelNo(Long majorId, Integer levelNo);
    List<PassedCourseDTO> getPassedCoursesByUserId(Long userId);
}
