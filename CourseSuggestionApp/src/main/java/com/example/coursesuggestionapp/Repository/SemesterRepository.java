package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Semester.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

}
