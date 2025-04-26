package com.example.coursesuggestionapp.Models.Auth;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterRequest {
    private String first_name;
    private String last_name;

    private String email;
    private String password;
    private Long studyMajor;

    private MultipartFile transcriptPdf;

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = last_name;
    }

}

