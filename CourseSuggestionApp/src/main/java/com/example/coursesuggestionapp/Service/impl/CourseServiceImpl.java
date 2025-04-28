package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Service.CourseService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDTO> getCoursesByMajorIdAndSemesterNo(Long majorId, Integer semesterNo) {
        List<Course> courses = courseRepository.findCoursesByMajorIdAndSemesterNo(majorId, semesterNo);

        return courses.stream()
                .map(course -> new CourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseLevel()))
                .sorted(Comparator.comparingLong(CourseDTO::getCourseId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getElectiveCoursesByMajorIdAndLevelNo(Long majorId, Integer levelNo) {
        List<Course> electiveCourses = courseRepository.findElectiveCoursesByMajorIdAndLevelNo(majorId, levelNo);

        return electiveCourses.stream()
                .map(course -> new CourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseLevel()))
                .sorted(Comparator.comparing(CourseDTO::getCourseLevel).thenComparing(CourseDTO::getCourseId))
                .collect(Collectors.toList());
    }
}
