package com.example.demo.repository;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query("SELECT new com.example.demo.dto.ClassroomDTO(c.id, c.subjectName, c.totalStudents, c.teacherId, u.name, c.startTime, c.endTime, c.meetingLink) " +
            "FROM Classroom c left JOIN User u ON c.teacherId = u.id")
    List<ClassroomDTO> findAllAsDTO();
}
