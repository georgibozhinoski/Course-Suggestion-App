package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.DTO.CourseInfoDTO;
import com.example.coursesuggestionapp.Models.DTO.PassedCourseDTO;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.MandatoryCourse.MandatoryCourse;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.MandatoryCourseRepository;
import com.example.coursesuggestionapp.Service.CourseService;
import com.example.coursesuggestionapp.Service.RatingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final MandatoryCourseRepository mandatoryCourseRepository;
    private final RatingService ratingService;

    public CourseServiceImpl(CourseRepository courseRepository, MandatoryCourseRepository mandatoryCourseRepository, RatingService ratingService) {
        this.courseRepository = courseRepository;
        this.mandatoryCourseRepository = mandatoryCourseRepository;
        this.ratingService = ratingService;
    }

    @Override
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

    @Override
    public List<PassedCourseDTO> getPassedCoursesByUserId(Long userId) {
        List<Object[]> passedCoursesWithGrades = courseRepository.findPassedCoursesWithGradeByUserId(userId);
        List<Long> mandatoryCourseIds = getMandatoryCourseIds();

        return passedCoursesWithGrades.stream()
                .map(result -> {
                    Long courseId = (Long) result[0];
                    String courseName = (String) result[1];
                    String courseLevel = (String) result[2];
                    String grade = String.valueOf(result[3]);

                    boolean isMandatory = mandatoryCourseIds.contains(courseId);
                    String type = isMandatory ? "M" : "E";

                    return new PassedCourseDTO(courseId, courseName, courseLevel, type, grade);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CourseInfoDTO getCourseDetails(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Double avgRating = ratingService.getCourseRating(courseId);
        Integer userRating = ratingService.getUserRatingForCourse(courseId, userId);

        return new CourseInfoDTO(course, avgRating, userRating);
    }

    @Override
    public void rateCourse(Long courseId, int rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        ratingService.rateCourse(userId, courseId, rating);
    }

    private List<Long> getMandatoryCourseIds() {
        return mandatoryCourseRepository.findAllMandatoryCourseIds();
    }

    @Override
    public Map<Integer, List<CourseDTO>> getCoursesByMajorId(Long majorId) {
        List<MandatoryCourse> mandatoryCourses = mandatoryCourseRepository.findAllByMajorId(majorId);
        List<Course> courses = courseRepository.findAll();

        Map<Long, CourseDTO> courseMap = courses.stream()
                .collect(Collectors.toMap(Course::getCourseId,
                        course -> new CourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseLevel())));

        return mandatoryCourses.stream()
                .collect(Collectors.groupingBy(
                        MandatoryCourse::getSemesterNo,
                        Collectors.mapping(
                                mc -> courseMap.get(mc.getCourseId()),
                                Collectors.toList()
                        )
                ));
    }
}
