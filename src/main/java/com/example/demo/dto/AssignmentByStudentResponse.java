package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentByStudentResponse {
    private String studentId;
    private String studentEmail;
    private String studentName;

    private Long assignmentId;

    private Long result;
    private Long status;
}
