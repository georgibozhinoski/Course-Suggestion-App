package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.Auth.AuthenticationRequest;
import com.example.coursesuggestionapp.Models.Auth.AuthenticationResponse;
import com.example.coursesuggestionapp.Models.Auth.RegisterRequest;
import com.example.coursesuggestionapp.Models.Entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void parseAndSaveUserCoursesFromPdf(MultipartFile transcriptPdf, User user);
}
