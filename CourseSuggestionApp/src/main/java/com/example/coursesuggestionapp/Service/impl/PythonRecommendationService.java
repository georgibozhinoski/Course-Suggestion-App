package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Configuration.PyRecommenderConfig;
import com.example.coursesuggestionapp.Models.DTO.CourseDTO;
import com.example.coursesuggestionapp.Models.DTO.PyRecommenderResponse;
import com.example.coursesuggestionapp.Models.DTO.RecommendedCourseDTO;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.MandatoryCourse.MandatoryCourse;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourse;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.MandatoryCourseRepository;
import com.example.coursesuggestionapp.Repository.UserCourseRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.RecommendationService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PythonRecommendationService implements RecommendationService {
    private final PyRecommenderConfig config;
    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;
    private final MandatoryCourseRepository mandatoryCourseRepository;

    private record RecommenderResponse(Course course, Double score) {}

    public PythonRecommendationService(
            PyRecommenderConfig config,
            RestTemplateBuilder builder,
            CourseRepository courseRepository, UserRepository userRepository,
            UserCourseRepository userCourseRepository, MandatoryCourseRepository mandatoryCourseRepository) {
        this.config = config;
        this.restTemplate = builder.build();
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.userCourseRepository = userCourseRepository;
        this.mandatoryCourseRepository = mandatoryCourseRepository;
    }

    private String getRecommenderBaseUrl() {
        return "http://" + config.getUrl() + ":" + config.getPort();
    }

    @PostConstruct
    private void initRecommender() {
        List<Course> courses = courseRepository.findAll();

        List<Map<String, String>> payload = courses.stream()
                .map(this::createPythonRecommenderPayload)
                .toList();

        Map<String, Object> requestBody = Map.of("courses", payload);

        String url = getRecommenderBaseUrl() + "/load_courses";
        try {
            restTemplate.postForEntity(url, requestBody, String.class);
        } catch (Exception e) {
            System.err.println("Python Recommender: Error initializing recommender: " + e.getMessage());
        }
        System.out.println("Python Recommender: Embeddings initialized");
    }

    @Override
    public List<RecommendedCourseDTO> getRecommendedCoursesForUser(Long userId, String semesterType, String level, String reason) {
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        List<Course> userCourses = userCourseRepository.findAllByUser(user).stream().map(UserCourse::getCourse).toList();

        List<String> courseCodes = userCourses.stream().map(Course::getCourseCode).toList();

        List<Long> mandatoryCourses = mandatoryCourseRepository.findAllByMajorId(user.getStudyMajor().getMajorId()).stream()
                .map(MandatoryCourse::getCourseId).toList();

        return fetchCoursesFromPythonService(courseCodes)
                .stream()
                .filter(c -> isNotMandatory(c, mandatoryCourses))
                .filter(c -> isAllowedSemester(c, semesterType))
                .filter(c -> isAllowedLevel(c, level))
                .filter(c -> arePrerequisitesFulfilled(c, userCourses))
                .map(this::toRecommendedCourseDTO)
                .toList();
    }

    @Override
    public List<RecommendedCourseDTO> getRecommendedCoursesForCourseList(List<Long> courseIds) {
        List<String> codes = courseIds.stream()
                .map(id -> courseRepository.findById(id).map(Course::getCourseCode).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return getRecommendedCoursesForCourseListByCode(codes);
    }

    @Override
    public List<RecommendedCourseDTO> getRecommendedCoursesForCourseListByCode(List<String> codes) {
        return fetchCoursesFromPythonService(codes)
                .stream()
                .map(this::toRecommendedCourseDTO)
                .toList();
    }

    @Override
    public void reloadRecommender() {
        initRecommender();
    }

    private List<RecommenderResponse> fetchCoursesFromPythonService(List<String> codes) {
        Map<String, Object> request = Map.of("selected_codes", codes);

        String url = getRecommenderBaseUrl() + "/recommend";
        PyRecommenderResponse[] response;

        try {
            response = restTemplate.postForObject(url, request, PyRecommenderResponse[].class);
        } catch (Exception e) {
            System.err.println("Recommendation request failed: " + e.getMessage());
            throw e;
        }

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(this::toRecommenderResponse)
                .toList();
    }

    private Map<String, String> createPythonRecommenderPayload(Course course) {
        String text = course.getCourseGoals() + " " + course.getCourseDescription();
        return Map.of("code", course.getCourseCode(), "text", text);
    }

    private RecommendedCourseDTO toRecommendedCourseDTO(RecommenderResponse response) {
        return new RecommendedCourseDTO(
                new CourseDTO(
                        response.course.getCourseId(),
                        response.course.getCourseName(),
                        response.course.getCourseLevel()
                ),
                Double.toString(response.score)
        );
    }

    private RecommenderResponse toRecommenderResponse(PyRecommenderResponse response) {
        Course course = toCourse(response);

        return new RecommenderResponse(course, response.getScore());
    }

    private Course toCourse(PyRecommenderResponse response) {
        return courseRepository.findByCourseCode(response.getCode()).orElseThrow(IllegalArgumentException::new);
    }

    private boolean isNotMandatory(RecommenderResponse response, List<Long> mandatoryCourses) {
        return !mandatoryCourses.contains(response.course.getCourseId());
    }

    private boolean isAllowedSemester(RecommenderResponse response, String semesterType) {
        return switch (semesterType) {
            case "WINTER" -> response.course.isWinter();
            case "SUMMER" -> !response.course.isWinter();
            default -> true;
        };
    }

    private boolean arePrerequisitesFulfilled(RecommenderResponse response, List<Course> userCourses) {
        String prerequisites = response.course.getPrerequisiteCredits();

        if (prerequisites == null || prerequisites.isBlank()) return true;

        Set<String> targetTerms = Set.of("ЕКТС", "кредити");
        List<String> words = List.of(prerequisites.split("\\s+"));
        if (words.stream().anyMatch(targetTerms::contains)) {
            int requiredCredits = words.stream()
                    .filter(c -> c.matches("\\d+"))
                    .mapToInt(Integer::parseInt)
                    .findFirst().orElseThrow(IllegalStateException::new);

            return userCourses.stream().mapToInt(Course::getCreditScore).sum() >= requiredCredits;
        }

        List<String> prerequisiteCoursesNames = Arrays.stream(prerequisites.split("или")).map(String::trim).toList();

        return userCourses.stream().map(Course::getCourseName).anyMatch(prerequisiteCoursesNames::contains);
    }

    private boolean isAllowedLevel(RecommenderResponse response, String level) {
        return "ALL".equalsIgnoreCase(level) ||
                Objects.equals(response.course.getCourseLevel(), level);
    }
}