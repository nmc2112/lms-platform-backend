package com.example.demo.repository;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query("SELECT new com.example.demo.dto.ClassroomDTO(c.id, c.subjectName, c.totalStudents, c.teacherId, u.name, c.startTime, c.endTime, c.meetingLink, c.description, c.imgUrl,c.until,c.dayOfWeek) " +
            "FROM Classroom c left JOIN User u ON c.teacherId = u.id")
    List<ClassroomDTO> findAllAsDTO();

    @Query("SELECT new com.example.demo.dto.ClassroomDTO(c.id, c.subjectName, c.totalStudents, c.teacherId, u.name, c.startTime, c.endTime, c.meetingLink, c.description, c.imgUrl,c.until,c.dayOfWeek) " +
            "FROM Classroom c left JOIN User u ON c.teacherId = u.id where c.id = :classroomId")
    ClassroomDTO findByIdAsDTO(@Param("classroomId") Long classroomId);

    @Query("SELECT c, u FROM Classroom c left JOIN User u ON c.teacherId = u.id")
    List<Object[]> testJoin();

    @Query(value = "SELECT c.* from classroom c " +
            "left join User u on u.id = c.teacherId " +
            "where trim(c.subjectName) = :subjectName " +
            "and trim(u.name) = :teacherName LIMIT 1", nativeQuery = true)
    Classroom findBySubjectNameAndTeacherName(@Param("subjectName") String subjectName, @Param("teacherName") String teacherName);

    @Query("SELECT new com.example.demo.dto.ClassroomDTO(c.id, c.subjectName, c.totalStudents, c.teacherId, u.name, c.startTime, c.endTime, c.meetingLink, c.description, c.imgUrl,c.until,c.dayOfWeek) " +
            "FROM Classroom c left JOIN User u ON c.teacherId = u.id " +
            "WHERE u.userClerkId = :userId")
    List<ClassroomDTO> findAllByTeacherIdAsDTO(@Param("userId") String userId);



}
