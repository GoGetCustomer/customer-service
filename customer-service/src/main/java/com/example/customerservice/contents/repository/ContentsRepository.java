package com.example.customerservice.contents.repository;

import com.example.customerservice.contents.entity.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    @Query("SELECT CONCAT(c.title, '|', c.writer) FROM Contents c")
    List<String> findAllTitlesAndWriters();
}
