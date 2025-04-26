package com.example.coursesuggestionapp.Models.Builders;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.Professor;
import com.example.coursesuggestionapp.Models.Entities.Semester.Semester;
import com.example.coursesuggestionapp.Models.Entities.Semester.SemesterId;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.ProfessorRepository;
import com.example.coursesuggestionapp.Repository.SemesterRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ibm.icu.text.Transliterator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CourseCsvImporter {

    @Autowired
    private SemesterRepository semesterRepository;

    private static final Transliterator TO_LATIN = Transliterator.getInstance("Cyrillic-Latin");

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public void importCoursesFromCsv(InputStream csvInputStream, StudyMajor studyMajor) throws IOException, CsvValidationException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String[] headers = csvReader.readNext();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                Course course = parseCourseFromCsvLine(line);
                if (course != null) {
                    courseRepository.save(course);
                    assignCourseToSemesters(course, studyMajor);

                }
            }
        }
    }

    private void assignCourseToSemesters(Course course, StudyMajor studyMajor) {
        int level = Integer.parseInt(course.getCourseLevel());
        boolean isWinter = course.isWinter();

        List<Integer> semesterNumbers = new ArrayList<>();

        if (level == 1) {
            semesterNumbers.add(isWinter ? 1 : 2);
        } else if (level == 2) {
            semesterNumbers.add(isWinter ? 3 : 4);
        } else if (level == 3) {
            if (isWinter) {
                semesterNumbers.add(5);
                semesterNumbers.add(7);
            } else {
                semesterNumbers.add(6);
                semesterNumbers.add(8);
            }
        }

        for (Integer semesterNo : semesterNumbers) {
            SemesterId semesterId = new SemesterId();
            semesterId.setStudyMajorId(studyMajor.getMajorId());
            semesterId.setSemesterNo(semesterNo);

            Optional<Semester> semesterOpt = semesterRepository.findById(semesterId);
            if (semesterOpt.isPresent()) {
                Semester semester = semesterOpt.get();
                semester.getCourses().add(course);
                course.getSemesters().add(semester);
                semesterRepository.save(semester); // persist the changes
            } else {
                System.err.println("Semester not found: " + semesterId);
            }
        }
    }

    private Course parseCourseFromCsvLine(String[] line) {

        String courseCode = line[4];

        // Check if course already exists
        if (courseRepository.findByCourseCode(courseCode).isPresent()) {
            return null; // Don't add duplicate course
        }

        Course course = new Course();

        course.setCourseName(line[0]);
        course.setCourseLevel(String.valueOf(Integer.parseInt(line[2])));
        course.setWinter(Boolean.parseBoolean(line[3]));
        course.setCourseCode(line[4]);

        String courseGoals = line[5];
        if (courseGoals.length() > 3500) {
            courseGoals = courseGoals.substring(0, 3500);
        }
        course.setCourseGoals(courseGoals);

        String courseDescription = line[6];
        if (courseDescription.length() > 3500) {
            courseDescription = courseDescription.substring(0, 3500);
        }
        course.setCourseDescription(courseDescription);


        try {
            course.setCreditScore((short) Double.parseDouble(line[7]));
        } catch (NumberFormatException e) {
            course.setCreditScore((short) 0);
        }

        if (!line[8].isBlank()) {
            try {
                course.setPrerequisiteCredits(line[8]);
            } catch (NumberFormatException e) {
                course.setPrerequisiteCredits(null);
            }
        } else {
            course.setPrerequisiteCredits(null);
        }

        String professorsStr = line.length > 9 ? line[9] : "";
        Set<Professor> professors = parseProfessors(professorsStr);
        course.setProfessors(professors);

        for (Professor prof : professors) {
            prof.getCourses().add(course);
        }

        return course;
    }

    private Set<Professor> parseProfessors(String professorsStr) {
        Set<Professor> professors = new HashSet<>();
        if (professorsStr == null || professorsStr.isBlank()) {
            return professors;
        }

        // Proffesors seperated by ; in scraped data
        String[] professorNames = professorsStr.split(";");

        for (String nameRaw : professorNames) {
            String name = nameRaw.trim();
            if (name.isEmpty()) continue;

            // Truncate if too long
            if (name.length() > 250) {
                name = name.substring(0, 250);
            }

            String finalName = name;
            Professor prof = professorRepository.findByProfessorName(name)
                    .orElseGet(() -> {
                        Professor newProf = new Professor();
                        newProf.setProfessorName(finalName);
                        newProf.setProfEmail(generateProfessorEmail(finalName));
                        return professorRepository.save(newProf);
                    });

            professors.add(prof);
        }
        return professors;
    }

    private String generateProfessorEmail(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return null;
        }

        // Remove academic titles
        String cleanedName = rawName.replaceAll("(?i)проф\\.?", "")
                .replaceAll("(?i)д-р\\.?", "")
                .replaceAll("(?i)ворн\\.?", "")
                .replaceAll("(?i)доц.\\.?", "")
                .replaceAll("\\s+", " ")
                .trim();

        String[] nameParts = cleanedName.split(" ");
        if (nameParts.length == 0) {
            return null;
        }

        StringBuilder emailBuilder = new StringBuilder();
        for (int i = 0; i < nameParts.length; i++) {
            emailBuilder.append(nameParts[i].toLowerCase(Locale.ROOT));
            if (i < nameParts.length - 1) {
                emailBuilder.append(".");
            }
        }

        emailBuilder.append("@finki.ukim.mk");

        String email = emailBuilder.toString();
        return TO_LATIN.transliterate(email);
    }
}

