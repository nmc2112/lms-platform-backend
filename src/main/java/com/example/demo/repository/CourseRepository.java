package com.example.demo.repository;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.CourseDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT new com.example.demo.dto.CourseDTO(c.id, c.userId, u.username, c.title, c.description, c.imageUrl, c.price, c.isPublished, c.categoryId, c.categoryId, c.createdAt, c.updateAt) " +
            "FROM Course c " +
            "left JOIN User u ON c.userId = u.userClerkId " +
            "WHERE u.userClerkId = :userId")
    List<CourseDTO> findAllCourseByTeacherIdAsDTO(@Param("userId") String userId);

    @Query("SELECT new com.example.demo.dto.CourseDTO(c.id, c.userId, u.name, c.title, c.description, c.imageUrl, c.price, c.isPublished, c.categoryId, ca.name, c.createdAt, c.updateAt) " +
            "FROM Course c " +
            "left JOIN User u ON c.userId = u.userClerkId " +
            "left JOIN Category ca ON c.categoryId = ca.id")
    List<CourseDTO> findAllAsDTO();

}
