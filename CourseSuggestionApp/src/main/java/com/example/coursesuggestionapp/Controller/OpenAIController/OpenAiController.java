package com.example.coursesuggestionapp.Controller.OpenAIController;

import com.example.coursesuggestionapp.Models.DTO.RecommendRequest;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourse;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.StudyMajorService;
import com.example.coursesuggestionapp.Service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/openai")
public class OpenAiController {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String model = "gpt-4.1-nano";
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudyMajorService studyMajorService;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiController(CourseRepository courseRepository, UserRepository userRepository, StudyMajorService studyMajorService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studyMajorService = studyMajorService;
    }

    @PostMapping("/recommend-courses")
    public ResponseEntity<String> recommendCourses(@RequestBody RecommendRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();

        Long userId = request.getUserId();
        Long majorId = studyMajorService.getStudyMajorIdByUserId(userId);


        int level = Integer.parseInt(request.getLevel().replaceAll("[^0-9]", ""));

        String semester = request.getSemester();
        List<Course> courses;
        if ("all".equals(semester)) {
            courses = courseRepository.findElectiveCoursesByMajorIdAndLevelNo(majorId, level);
        } else {
            if(semester.equalsIgnoreCase("winter")){
                courses = courseRepository.findElectiveCoursesByMajorIdAndCourseLevelAndWinterIsTrue(majorId, level);
            }else{
                courses = courseRepository.findElectiveCoursesByMajorIdAndCourseLevelAndWinterIsFalse(majorId, level);
            }
        }

        String allCourseNames = courses.stream()
                .map(Course::getCourseName)
                .collect(Collectors.joining(", "));

        String allUserPassedCourses = userRepository.findById(userId)
                .map(User::getPassedCourses)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .stream()
                .map(UserCourse::getCourse)
                .map(Course::getCourseName)
                .collect(Collectors.joining(", "));

        String reason = request.getReasons();

        String prompt = String.format(
                "The user has passed the following courses: %s. " +
                        "Here are the available courses: %s. " +
                        "Please recommend 5 elective courses that the user should take next. his message is: %s " +
                        "Reply only with the name of the course you are suggesting followed by a '-' and the reason why, and use a comma (',') to separate the courses.",
                allUserPassedCourses, allCourseNames, reason);


        String requestBody = String.format("{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 100}",
                model, prompt);

        System.out.println("Request Body: " + requestBody);

        okhttp3.Request apiRequest = new okhttp3.Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(okhttp3.RequestBody.create(requestBody, okhttp3.MediaType.parse("application/json")))
                .build();

        try {
            okhttp3.Response response = httpClient.newCall(apiRequest).execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                System.out.println("OpenAI Response: " + responseBody);

                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                String recommendedCourses = jsonResponse.path("choices").get(0).path("message").path("content").asText();

                System.out.println("Recommended courses: " + recommendedCourses);

                String responseString = objectMapper.writeValueAsString(Collections.singletonMap("recommendations", recommendedCourses));

                return ResponseEntity.ok(responseString);
            } else {
                System.out.println("Request failed with code: " + response.code());
                return ResponseEntity.status(response.code()).body("Error: " + response.message());
            }

        } catch (IOException e) {
            System.out.println("Error communicating with OpenAI API: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with OpenAI API.");
        }

    }
}
