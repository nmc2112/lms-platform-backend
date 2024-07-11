package com.example.demo.repository;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT new com.example.demo.dto.AssignmentDTO(a.id, a.name, a.duration, a.numberOfQuestions, a.userId, a.classroomId, c.subjectName, a.quesCategoryId, q.name, a.createdAt, a.updatedAt) " +
            "FROM Assignment a " +
            "left JOIN Classroom c ON c.id = a.classroomId " +
            "left JOIN QuestionCategory q ON q.id = a.quesCategoryId " +
            "WHERE a.classroomId in :classroomIds")
    List<AssignmentDTO> findAllByClassroomIds(@Param("classroomIds") List<Long> classroomIds);

    @Query("SELECT a FROM Assignment a where a.classroomId=:classroomId order by a.createdAt asc ")
    List<Assignment> findAllByClassroomId(@Param("classroomId") Long classroomId);

    @Query("SELECT new com.example.demo.dto.AssignmentDTO(a.id, a.name, a.duration, a.numberOfQuestions, a.userId, a.classroomId, c.subjectName, a.quesCategoryId, q.name, a.createdAt, a.updatedAt) " +
            " FROM Assignment a " +
            "LEFT JOIN Classroom c ON c.id = a.classroomId " +
            "LEFT JOIN QuestionCategory q ON q.id = a.quesCategoryId " +
            "WHERE a.userId = :teacherId")
    List<AssignmentDTO> findAllByTeacherId(@Param("teacherId") String teacherId);

}
