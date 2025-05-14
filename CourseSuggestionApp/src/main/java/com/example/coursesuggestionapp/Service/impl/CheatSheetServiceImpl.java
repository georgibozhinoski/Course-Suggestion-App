package com.example.coursesuggestionapp.Service.impl;
import com.example.coursesuggestionapp.Models.DTO.CheatSheetInfoDTO;
import com.example.coursesuggestionapp.Models.DTO.CheatSheetRequest;
import com.example.coursesuggestionapp.Models.Entities.*;
import com.example.coursesuggestionapp.Repository.*;
import com.example.coursesuggestionapp.Service.CheatSheetService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheatSheetServiceImpl implements CheatSheetService {

    private final CheatSheetRepository cheatSheetRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final FileRepository fileRepo;

    public CheatSheetServiceImpl(CheatSheetRepository cheatSheetRepo, UserRepository userRepo, CourseRepository courseRepo, FileRepository fileRepo) {
        this.cheatSheetRepo = cheatSheetRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.fileRepo = fileRepo;
    }

    @Override
    public CheatSheet createCheatSheet(CheatSheetRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CheatSheet cheatSheet = new CheatSheet(
                request.getSheetContent(),
                LocalDateTime.now(),
                0,
                user,
                course
        );

        cheatSheet = cheatSheetRepo.save(cheatSheet);

        if (request.getFiles() != null) {
            List<File> savedFiles = new ArrayList<>();
            for (var file : request.getFiles()) {
                try {
                    File newFile = new File(file.getOriginalFilename(), file.getBytes(), cheatSheet);
                    savedFiles.add(fileRepo.save(newFile));
                } catch (IOException e) {
                    throw new RuntimeException("Error saving file: " + file.getOriginalFilename(), e);
                }
            }
            cheatSheet.setFiles(savedFiles);
        }

        return cheatSheetRepo.save(cheatSheet);
    }

    @Override
    public List<CheatSheetInfoDTO> getCheatSheetsByCourse(Long courseId) {
        return cheatSheetRepo.findByCourseCourseId(courseId).stream().map(this::toInfoDTO).toList();
    }

    @Override
    public List<CheatSheet> getAllCheatSheets() {
        return cheatSheetRepo.findAll();
    }

    public void likeCheatSheet(Long sheetId) {
        CheatSheet cheatSheet = cheatSheetRepo.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("CheatSheet not found"));

        if (cheatSheet.getSheetLikes() == null) {
            cheatSheet.setSheetLikes(1);
        } else {
            cheatSheet.setSheetLikes(cheatSheet.getSheetLikes() + 1);
        }

        cheatSheetRepo.save(cheatSheet);
    }

    @Override
    public byte[] getFilesByCheatSheet(Long sheetId) {
        CheatSheet sheet = cheatSheetRepo.findById(sheetId).orElseThrow(IllegalArgumentException::new);
        List<File> files = sheet.getFiles();

        if (files.isEmpty()) throw new IllegalStateException("No files found for this cheat sheet");

        return files.stream()
                .map(File::getContent)
                .findFirst().get();
    }

    private CheatSheetInfoDTO toInfoDTO(CheatSheet cheatSheet) {
        return new CheatSheetInfoDTO(
                cheatSheet.getSheetContent(),
                cheatSheet.getSheetId(),
                cheatSheet.getSheetDate(),
                cheatSheet.getSheetLikes(),
                cheatSheet.getUser().getId(),
                cheatSheet.getCourse().getCourseId()
        );
    }
}