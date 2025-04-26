package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {

}
