package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.StudyMajorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyMajorServiceImpl implements StudyMajorService {
    private final StudyMajorRepository repository;
    private final UserRepository userRepository;

    @Override
    public StudyMajor findByMajorName(String majorName) {
        return repository.findByMajorName(majorName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public StudyMajor findByMajorId(Long majorId) {
        return repository.findByMajorId(majorId).orElseThrow(IllegalArgumentException::new);
    }

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
