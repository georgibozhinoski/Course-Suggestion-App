package com.example.coursesuggestionapp.Controller.Auth;

import com.example.coursesuggestionapp.Models.Auth.AuthenticationRequest;
import com.example.coursesuggestionapp.Models.Auth.AuthenticationResponse;
import com.example.coursesuggestionapp.Models.Auth.RegisterRequest;
import com.example.coursesuggestionapp.Service.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> register(@ModelAttribute RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(Collections.singletonMap("token", response.getToken()));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}
