package com.example.coursesuggestionapp.Controller.StudyMajor;

import com.example.coursesuggestionapp.Service.StudyMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/study-major")
public class StudyMajorController {

    @Autowired
    private StudyMajorService studyMajorService;

    @GetMapping("/{userId}")
    public Long getStudyMajorIdByUserId(@PathVariable Long userId) {
        return studyMajorService.getStudyMajorIdByUserId(userId);
    }
}
