package com.example.demo.repository;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.AssignmentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentStudentRepository extends JpaRepository<AssignmentStudent, Long> {

    @Query("SELECT as1 FROM AssignmentStudent as1" +
            " LEFT JOIN User u ON u.id = as1.studentId "+
            " WHERE as1.assignmentId = :assignmentId AND u.userClerkId = :studentId " )
    AssignmentStudent findByAssignmentIdAndStudentId(@Param("assignmentId") Long assignmentId, @Param("studentId")  String studentId);

}
