package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.DTO.PassedCourseDTO;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<CourseDTO> getCoursesByMajorIdAndSemesterNo(Long majorId, Integer semesterNo);
    Map<Integer, List<CourseDTO>> getCoursesByMajorId(Long majorId);
    List<CourseDTO> getElectiveCoursesByMajorIdAndLevelNo(Long majorId, Integer levelNo);
    List<PassedCourseDTO> getPassedCoursesByUserId(Long userId);
}
