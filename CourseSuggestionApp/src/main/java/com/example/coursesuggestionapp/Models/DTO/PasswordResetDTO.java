package com.example.coursesuggestionapp.Models.DTO;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String oldPassword;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}