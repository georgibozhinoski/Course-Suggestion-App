package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.DTO.PasswordResetDTO;
import com.example.coursesuggestionapp.Models.DTO.UserRequest;
import com.example.coursesuggestionapp.Models.DTO.UserResponse;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String passwordReset(PasswordResetDTO passwordResetDTO) {

        User user = userRepository.findByEmail(passwordResetDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(passwordResetDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        if (passwordEncoder.matches(passwordResetDTO.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as old password");
        }

        if (passwordResetDTO.getNewPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        userRepository.save(user);

        return "Password changed Successfully";
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse)
                .toList();
    }

    @Override
    public Optional<UserResponse> getUserById(Long userId) {
        return userRepository.findById(userId).map(this::mapToUserResponse);
    }

    @Override
    public UserResponse saveUser(UserRequest user) {
        User u = userRepository.findById(user.getId()).orElseThrow();
        if (user.getNewFirstName() != null) {
            u.setFirstName(user.getNewFirstName());
        }
        if (user.getNewLastName() != null) {
            u.setLastName(user.getNewLastName());
        }
        userRepository.save(u);
        return new UserResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail());
    }

    @Override
    public String deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return "User deleted successfully.";
        } else {
            return "User not found.";
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
