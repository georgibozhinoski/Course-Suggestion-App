package com.example.coursesuggestionapp.Models.DTO;
import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String newFirstName;
    private String newLastName;
    private Long newStudyMajorId;
}
