package com.example.coursesuggestionapp.Controller.Course;

import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.DTO.CourseInfoDTO;
import com.example.coursesuggestionapp.Models.DTO.PassedCourseDTO;
import com.example.coursesuggestionapp.Service.CourseService;
import com.example.coursesuggestionapp.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/by-major/{majorId}/semester/{semesterNo}")
    public List<CourseDTO> getMandatoryCoursesByMajorIdAndSemesterNo(@PathVariable Long majorId, @PathVariable Integer semesterNo) {
        return courseService.getCoursesByMajorIdAndSemesterNo(majorId, semesterNo);
    }

    @GetMapping("/by-major/{majorId}/elective/{levelNo}")
    public List<CourseDTO> getElectiveCoursesByMajorIdAndLevelNo(@PathVariable Long majorId, @PathVariable Integer levelNo) {
        return courseService.getElectiveCoursesByMajorIdAndLevelNo(majorId, levelNo);
    }

    @GetMapping("/passed-courses/user/{userId}")
    public List<PassedCourseDTO> getPassedCoursesByUserId(@PathVariable Long userId) {
        return courseService.getPassedCoursesByUserId(userId);
    }

    @GetMapping("/by-major/{majorId}/all")
    public Map<Integer, List<CourseDTO>> getMandatoryCoursesByMajorId(@PathVariable Long majorId) {
        return courseService.getCoursesByMajorId(majorId);
    }

    @GetMapping("/details/{courseId}")
    public CourseInfoDTO getCourseDetails(@PathVariable Long courseId) {
        return courseService.getCourseDetails(courseId);
    }

    @PostMapping("/{courseId}/rate")
    public CourseInfoDTO rateCourse(@PathVariable Long courseId, @RequestParam int rating) {
        courseService.rateCourse(courseId, rating);
        return courseService.getCourseDetails(courseId);
    }
}
