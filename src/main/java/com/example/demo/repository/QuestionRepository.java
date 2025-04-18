package com.example.demo.repository;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.entity.Question;
import com.example.demo.entity.QuestionCategory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.entity")
public interface QuestionRepository extends JpaRepository<Question, String> {

    @Query("SELECT q FROM Question q " +
            "left JOIN Assignment a ON a.quesCategoryId = q.quesCategoryId " +
            "WHERE a.id = :assignmentId ORDER BY RAND() LIMIT :numberOfQuestions")
    List<Question> findQuestionsByAssignment(@Param("assignmentId") Long assignmentId, @Param("numberOfQuestions") Long numberOfQuestions);


}
