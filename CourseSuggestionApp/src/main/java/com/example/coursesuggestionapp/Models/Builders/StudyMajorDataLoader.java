package com.example.coursesuggestionapp.Models.Builders;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StudyMajorDataLoader implements CommandLineRunner {

    private final StudyMajorRepository studyMajorRepository;

    public StudyMajorDataLoader(StudyMajorRepository studyMajorRepository) {
        this.studyMajorRepository = studyMajorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (studyMajorRepository.count() == 0) {
            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Software engineering and information systems")
                    .accreditationYear(2024)  // or any year you want
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Internet, networks and security")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Applied information technologies")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Informatics education")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Computer engineering")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Computer science")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Professional programming studies")
                    .accreditationYear(2024)
                    .build());
        }
    }
}