package com.example.coursesuggestionapp.Controller.User;

import com.example.coursesuggestionapp.Models.DTO.PasswordResetDTO;
import com.example.coursesuggestionapp.Models.DTO.UserRequest;
import com.example.coursesuggestionapp.Models.DTO.UserResponse;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.AuthenticationService;
import com.example.coursesuggestionapp.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final StudyMajorRepository studyMajorRepository;
    public UserController(UserService userService, AuthenticationService authenticationService, UserRepository userRepository, StudyMajorRepository studyMajorRepository) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.studyMajorRepository = studyMajorRepository;
    }

    @PutMapping("/change-password")
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

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @RequestParam("id") Long id,
            @RequestParam("newFirstName") String newFirstName,
            @RequestParam("newLastName") String newLastName,
            @RequestParam("newStudyMajorId") Long newStudyMajorId,
            @RequestPart(value = "transcriptPdf", required = false) MultipartFile transcriptPdf
    ) {
        if (transcriptPdf != null) {
            String contentType = transcriptPdf.getContentType();
            System.out.println("Received Content-Type: " + contentType);
        }

        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

        if (transcriptPdf != null && !transcriptPdf.isEmpty()) {
            String contentType = transcriptPdf.getContentType();
            if (!"application/pdf".equals(contentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be a PDF");
            }
            try {
                authenticationService.parseAndSaveUserCoursesFromPdf(transcriptPdf, user);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid PDF format or content", e);
            }
        }

        user.setFirstName(newFirstName);
        user.setLastName(newLastName);

        StudyMajor major = studyMajorRepository.findByMajorId(newStudyMajorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Study Major not found with id: " + newStudyMajorId));
        user.setStudyMajor(major);

        userRepository.save(user);

        return ResponseEntity.ok(
                new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getStudyMajor().getMajorName())
        );
    }




}
