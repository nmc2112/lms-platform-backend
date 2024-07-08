package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class AssignmentDTO {
    private Long id;

    private String name;

    private Long duration;

    private Long numberOfQuestions;

    private String userId;

    public AssignmentDTO(Long id, String name, Long duration, Long numberOfQuestions, String userId, Long classroomId, String classroomSubjectName, String quesCategoryId, String quesCategoryName, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.numberOfQuestions = numberOfQuestions;
        this.userId = userId;
        this.classroomId = classroomId;
        this.classroomSubjectName = classroomSubjectName;
        this.quesCategoryId = quesCategoryId;
        this.quesCategoryName = quesCategoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private Long classroomId;

    private String classroomSubjectName;

    private String quesCategoryId;
    private String quesCategoryName;

    private Date createdAt;

    private Date updatedAt;

    private Long result;

    private Long status;
}
