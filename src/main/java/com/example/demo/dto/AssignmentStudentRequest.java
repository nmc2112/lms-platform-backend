package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class AssignmentStudentRequest {
    private Long id;

    private Long assignmentId;

    private String studentId;

    private Long status;

    private Long result;

    private String detail;
}
