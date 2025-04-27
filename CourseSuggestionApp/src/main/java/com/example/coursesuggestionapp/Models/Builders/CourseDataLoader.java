package com.example.coursesuggestionapp.Models.Builders;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.StudyMajorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Component
@Order(3)
public class CourseDataLoader implements CommandLineRunner {

    @Autowired
    private StudyMajorRepository studyMajorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCsvImporter courseCsvImporter;

    private final String[] csvFiles = {
            "MajorsScrapedData/Software engineering and information systems(4years).csv",
            "MajorsScrapedData/Интернет, мрежи и безбедност(4годишни).csv",
            "MajorsScrapedData/Информатичка едукација(4годишни).csv",
            "MajorsScrapedData/Компјутерски науки(4годишни).csv",
            "MajorsScrapedData/Компјутерско инженерство(4годишни).csv",
            "MajorsScrapedData/Примена на информациски технологии(4годишни).csv",
            "MajorsScrapedData/Софтверско инженерство и информациски системи(4годишни).csv",
            "MajorsScrapedData/Стручни студии за програмирање(3годишни).csv"
    };

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            for (String csvFile : csvFiles) {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(csvFile)) {
                    if (is == null) {
                        System.err.println("CSV file not found: " + csvFile);
                        continue;
                    }

                    String majorName = extractMajorNameFromCsvPath(csvFile);
                    Optional<StudyMajor> studyMajorOpt = studyMajorRepository.findByMajorName(majorName);

                    if (studyMajorOpt.isEmpty()) {
                        System.err.println("No StudyMajor found for name: " + majorName);
                        continue;
                    }

                    StudyMajor studyMajor = studyMajorOpt.get();

                    courseCsvImporter.importCoursesFromCsv(is, studyMajor);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Course data imported successfully.");
        } else {
            System.out.println("Courses already present in DB; skipping import.");
        }
    }



    private String extractMajorNameFromCsvPath(String csvFilePath) {
        // Remove folder path
        String fileName = Paths.get(csvFilePath).getFileName().toString();
        // fileName example: "Software engineering and information systems(4years).csv"

        // Remove the ".csv" extension
        String withoutExtension = fileName.replaceFirst("\\.csv$", "");

        // Remove the suffix in parentheses (like "(4years)" or "(4годишни)")
        return withoutExtension.replaceAll("\\(.*\\)$", "").trim();
    }

}
