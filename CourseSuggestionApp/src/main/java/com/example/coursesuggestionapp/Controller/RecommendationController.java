package com.example.coursesuggestionapp.Controller;

import com.example.coursesuggestionapp.Models.DTO.RecommendCoursesRequest;
import com.example.coursesuggestionapp.Models.DTO.RecommendedCourseDTO;
import com.example.coursesuggestionapp.Service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;


    @PostMapping()
    public ResponseEntity<List<RecommendedCourseDTO>> getRecommendationsForUserWithFilters(@RequestBody RecommendCoursesRequest request) {
        try {
            return ResponseEntity.ok(recommendationService.getRecommendedCoursesForUser(request.getUserId(), request.getSemester(), request.getLevel(), request.getReason()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/user")
    public ResponseEntity<List<RecommendedCourseDTO>> getRecommendationsForUser(@RequestBody Long userId) {
        try {
            return ResponseEntity.ok(recommendationService.getRecommendedCoursesForUser(userId, "ALL", "ALL", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/codes")
    public ResponseEntity<List<RecommendedCourseDTO>> getRecommendationsFromCodeList(@RequestBody List<String> selectedCourseCodes) {
        if (selectedCourseCodes == null || selectedCourseCodes.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(recommendationService.getRecommendedCoursesForCourseListByCode(selectedCourseCodes));
    }

    @PostMapping("/reload")
    public ResponseEntity<Void> reloadEmbeddings() {
        recommendationService.reloadRecommender();
        return ResponseEntity.ok().build();
    }
}
