package com.example.coursesuggestionapp.Models.DTO;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studyMajor;

    public UserResponse(Long id, String firstName, String lastName, String email, String studyMajor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studyMajor = studyMajor;
    }

}

