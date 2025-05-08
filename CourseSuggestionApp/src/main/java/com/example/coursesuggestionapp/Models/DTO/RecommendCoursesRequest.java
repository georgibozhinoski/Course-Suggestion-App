package com.example.coursesuggestionapp.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendCoursesRequest {
    private Long userId;
    private String semester;
    private String level;
    private String reason;
}
