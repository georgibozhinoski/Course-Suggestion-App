package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Auth.AuthenticationRequest;
import com.example.coursesuggestionapp.Models.Auth.AuthenticationResponse;
import com.example.coursesuggestionapp.Models.Auth.RegisterRequest;
import com.example.coursesuggestionapp.Models.ENUM.Role;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.AuthenticationService;
import com.example.coursesuggestionapp.Service.JWTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudyMajorRepository studyMajorRepository;
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, StudyMajorRepository studyMajorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.studyMajorRepository = studyMajorRepository;
    }


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        if (request.getStudyMajor() == null) {
            throw new IllegalArgumentException("You must select a study major");
        }

        StudyMajor studyMajor = studyMajorRepository.findById(request.getStudyMajor())
                .orElseThrow(() -> new IllegalArgumentException("Study major not found"));

        var user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );

        user.setStudyMajor(studyMajor);

        userRepository.save(user);

        MultipartFile file = request.getTranscriptPdf();
        if (file != null && !file.isEmpty()) {
            List<Map<String, Object>> subjects = parseTranscriptPdfToJsonCompatibleList(file);

            // Convert to JSON string
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(subjects);
                System.out.println("Extracted Subjects JSON:\n" + json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    private List<Map<String, Object>> parseTranscriptPdfToJsonCompatibleList(MultipartFile file) {
        List<Map<String, Object>> subjects = new ArrayList<>();

        try {
            InputStream inputStream = file.getInputStream();
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            Pattern pattern = Pattern.compile("(\\d+)\\.\\s+(.*?)\\s+(\\d{1,2})\\s+(\\d{1,2}\\.\\d{2})");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                Map<String, Object> subject = new HashMap<>();
                subject.put("name", matcher.group(2).trim());
                subject.put("grade", Integer.parseInt(matcher.group(3)));
                subject.put("credits", Double.parseDouble(matcher.group(4)));

                subjects.add(subject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subjects;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authResponse = new AuthenticationResponse(jwtToken);
        return authResponse;
    }

}
