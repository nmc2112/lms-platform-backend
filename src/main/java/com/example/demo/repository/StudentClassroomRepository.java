package com.example.demo.repository;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentClassroom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentClassroomRepository extends JpaRepository<StudentClassroom, Long> {
    @Query(value = "select studentId from student_classroom where classroomId=:classroomId", nativeQuery = true)
    List<Long> findStudentsByClassroomId(@Param("classroomId") Long classroomId);


    @Query(value = "select classroomId from student_classroom left join user on user.id = student_classroom.studentId  where user.userClerkId=:userId", nativeQuery = true)
    List<Long> findClassroomsByStudentId(@Param("userId") String userId);

    @Query(value = "SELECT new com.example.demo.dto.ClassroomDTO(c.id, c.subjectName, c.totalStudents, c.teacherId, u.name, c.startTime, c.endTime, c.meetingLink, c.description, c.imgUrl, c.until, c.dayOfWeek) " +
            "FROM Classroom c " +
            "left JOIN StudentClassroom s ON c.id = s.classroomId " +
            "left JOIN User u ON s.studentId = u.id " +
            "WHERE u.userClerkId = :userId")
    List<ClassroomDTO> findAllByStudentIdAsDTO(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM student_classroom WHERE classroomId = :classroomId AND studentId = :studentId", nativeQuery = true)
    void deleteByStudentIdAndClassroomId(@Param("studentId") Long studentId, @Param("classroomId") Long classroomId);
}
