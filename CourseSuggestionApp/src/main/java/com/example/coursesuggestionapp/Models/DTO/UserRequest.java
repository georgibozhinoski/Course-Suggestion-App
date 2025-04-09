package com.example.coursesuggestionapp.Models.DTO;
import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String newFirstName;
    private String newLastName;

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public Long getId() {
        return id;
    }

}
