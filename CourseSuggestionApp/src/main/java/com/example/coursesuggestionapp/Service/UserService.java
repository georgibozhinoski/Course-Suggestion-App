package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.PasswordResetDTO;
import com.example.coursesuggestionapp.Models.DTO.UserRequest;
import com.example.coursesuggestionapp.Models.DTO.UserResponse;
import java.util.List;
import java.util.Optional;

public interface UserService {
    String passwordReset(PasswordResetDTO passwordResetDTO);
    List<UserResponse> getAllUsers();
    Optional<UserResponse> getUserById(Long userId);
    UserResponse saveUser(UserRequest user);
    String deleteUser(Long userId);
}
