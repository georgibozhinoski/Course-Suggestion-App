package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.PasswordResetDTO;

public interface UserService {
    String passwordReset(PasswordResetDTO passwordResetDTO);
}
