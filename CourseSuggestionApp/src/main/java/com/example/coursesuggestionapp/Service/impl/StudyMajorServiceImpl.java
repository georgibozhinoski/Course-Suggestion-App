package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import com.example.coursesuggestionapp.Service.StudyMajorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyMajorServiceImpl implements StudyMajorService {
    private final StudyMajorRepository repository;

    @Override
    public StudyMajor findByMajorName(String majorName) {
        return repository.findByMajorName(majorName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public StudyMajor findByMajorId(Long majorId) {
        return repository.findByMajorId(majorId).orElseThrow(IllegalArgumentException::new);
    }
}
