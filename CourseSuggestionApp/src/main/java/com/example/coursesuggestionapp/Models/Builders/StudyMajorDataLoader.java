package com.example.coursesuggestionapp.Models.Builders;

import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StudyMajorDataLoader implements CommandLineRunner {

    private final StudyMajorRepository studyMajorRepository;
    private final EntityManager entityManager;

    public StudyMajorDataLoader(StudyMajorRepository studyMajorRepository, EntityManager entityManager) {
        this.studyMajorRepository = studyMajorRepository;
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        entityManager.createNativeQuery("ALTER TABLE study_major ALTER COLUMN major_id RESTART WITH 1").executeUpdate();


        if (studyMajorRepository.count() == 0) {
            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Software engineering and information systems")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Интернет, мрежи и безбедност")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Информатичка едукација")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Компјутерски науки")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Компјутерско инженерство")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Примена на информациски технологии")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Софтверско инженерство и информациски системи")
                    .accreditationYear(2024)
                    .build());

            studyMajorRepository.save(StudyMajor.builder()
                    .majorName("Стручни студии за програмирање")
                    .accreditationYear(2024)
                    .build());
        }
    }
}