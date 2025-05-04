package com.example.coursesuggestionapp.Models.DTO;

import com.example.coursesuggestionapp.Service.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendRequest {
    private String semester;
    private String level;
    private String reasons;
    private Long userId;
}



