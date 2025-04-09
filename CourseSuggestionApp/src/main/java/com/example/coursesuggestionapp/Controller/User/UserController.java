package com.example.coursesuggestionapp.Controller.User;

import com.example.coursesuggestionapp.Models.DTO.PasswordResetDTO;
import com.example.coursesuggestionapp.Models.DTO.UserRequest;
import com.example.coursesuggestionapp.Models.DTO.UserResponse;
import com.example.coursesuggestionapp.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        return ResponseEntity.ok(userService.passwordReset(passwordResetDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<UserResponse>> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUserProfile(@RequestBody UserRequest updatedUser) {
        return ResponseEntity.ok(userService.saveUser(updatedUser));
    }

}
