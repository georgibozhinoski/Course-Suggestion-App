package com.example.coursesuggestionapp.Models.Builders;

import com.example.coursesuggestionapp.Models.Entities.Semester.Semester;
import com.example.coursesuggestionapp.Models.Entities.Semester.SemesterId;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.SemesterRepository;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)  // Runs after StudyMajorDataLoader which has @Order(1)
public class SemesterDataLoader implements CommandLineRunner {

    private final SemesterRepository semesterRepository;
    private final StudyMajorRepository studyMajorRepository;

    public SemesterDataLoader(SemesterRepository semesterRepository, StudyMajorRepository studyMajorRepository) {
        this.semesterRepository = semesterRepository;
        this.studyMajorRepository = studyMajorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (semesterRepository.count() == 0) {
            for (StudyMajor major : studyMajorRepository.findAll()) {
                for (int semesterNo = 1; semesterNo <= 8; semesterNo++) {
                    SemesterId semesterId = new SemesterId();
                    semesterId.setStudyMajorId(major.getMajorId());
                    semesterId.setSemesterNo(semesterNo);

                    boolean isWinter = (semesterNo % 2 == 1);

                    Semester semester = new Semester();
                    semester.setSemesterId(semesterId);
                    semester.setStudyMajor(major);
                    semester.setIsWinter(isWinter);
                    semester.setNumElectiveCourses(0);

                    semesterRepository.save(semester);
                }
            }
            System.out.println("Semesters loaded successfully.");
        } else {
            System.out.println("Semesters already present; skipping load.");
        }
    }
}

