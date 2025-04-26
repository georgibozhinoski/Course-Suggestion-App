package com.example.coursesuggestionapp.Models.Auth;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private MultipartFile transcriptPdf;

    //Getter for  transcriptPdf
    public MultipartFile getTranscriptPdf() {
        return transcriptPdf;
    }

    //Setter for transcriptPdf
    public void setTranscriptPdf(MultipartFile transcriptPdf) {
        this.transcriptPdf = transcriptPdf;
    }

    // Getter for firstName
    public String getFirstName() {
        return firstName;
    }

    // Setter for firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter for lastName
    public String getLastName() {
        return lastName;
    }

    // Setter for lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}

