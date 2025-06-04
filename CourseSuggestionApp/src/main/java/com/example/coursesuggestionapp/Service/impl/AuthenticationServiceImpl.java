package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Auth.AuthenticationRequest;
import com.example.coursesuggestionapp.Models.Auth.AuthenticationResponse;
import com.example.coursesuggestionapp.Models.Auth.RegisterRequest;
import com.example.coursesuggestionapp.Models.ENUM.Role;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourse;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import com.example.coursesuggestionapp.Repository.UserCourseRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.AuthenticationService;
import com.example.coursesuggestionapp.Service.JWTService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourseId;


import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudyMajorRepository studyMajorRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, StudyMajorRepository studyMajorRepository, CourseRepository courseRepository, UserCourseRepository userCourseRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.studyMajorRepository = studyMajorRepository;
        this.courseRepository = courseRepository;
        this.userCourseRepository = userCourseRepository;
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
        user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        MultipartFile file = request.getTranscriptPdf();
        if (file != null && !file.isEmpty()) {
            parseAndSaveUserCoursesFromPdf(file, user);
        }

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, user.getId());
    }

    public void parseAndSaveUserCoursesFromPdf(MultipartFile file, User user) {
        List<UserCourse> userCourses = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            Pattern pattern = Pattern.compile("(\\d+)\\.\\s+(.*?)\\s+(\\d{1,2})\\s+(\\d{1,2}\\.\\d{2})");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String courseName = matcher.group(2).trim();
                Integer grade = Integer.parseInt(matcher.group(3));

                Optional<Course> courseOpt = courseRepository.findByCourseName(courseName);
                if (courseOpt.isEmpty()) {
                    continue;
                }
                Course course = courseOpt.get();

                UserCourseId userCourseId = new UserCourseId();
                userCourseId.setUserId(user.getId());
                userCourseId.setCourseId(course.getCourseId());

                UserCourse userCourse = new UserCourse();
                userCourse.setId(userCourseId);
                userCourse.setUser(user);
                userCourse.setCourse(course);
                userCourse.setGrade(grade);

                userCourseRepository.save(userCourse);

                userCourses.add(userCourse);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userCourses.isEmpty()) {
            throw new IllegalArgumentException("No valid courses found in the PDF");
        }

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
        return new AuthenticationResponse(jwtToken, user.getId());
    }

}
