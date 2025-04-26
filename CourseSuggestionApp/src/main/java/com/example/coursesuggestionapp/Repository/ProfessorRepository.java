package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}
