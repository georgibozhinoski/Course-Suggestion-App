package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.StudyMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyMajorServiceImpl implements StudyMajorService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long getStudyMajorIdByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        StudyMajor studyMajor = user.getStudyMajor();

        if (studyMajor == null) {
            throw new RuntimeException("Study Major not found for user with id " + userId);
        }

        return studyMajor.getMajorId();
    }
}
