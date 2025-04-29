package com.example.coursesuggestionapp.Models.DTO;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}