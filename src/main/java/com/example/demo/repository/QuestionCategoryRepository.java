package com.example.demo.repository;

import com.example.demo.entity.QuestionCategory;
import com.example.demo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.entity")
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

    @Query(value = "SELECT q.name FROM  quescategory q WHERE userId = :userId", nativeQuery = true)
    List<String> findByUserId(@Param("userId") String userId);


}
