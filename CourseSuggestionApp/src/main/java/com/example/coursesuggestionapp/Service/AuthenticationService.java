package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.Auth.AuthenticationRequest;
import com.example.coursesuggestionapp.Models.Auth.AuthenticationResponse;
import com.example.coursesuggestionapp.Models.Auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
