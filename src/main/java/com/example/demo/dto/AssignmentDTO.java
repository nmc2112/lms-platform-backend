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

    private int duration;

    private int numberOfQuestions;

    private String userId;

    private Long classroomId;

    private String classroomSubjectName;

    private String quesCategoryId;
    private String quesCategoryName;

    private Date createdAt;

    private Date updatedAt;
}
