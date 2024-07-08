package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "assignment_student")
@Getter
@Setter
public class AssignmentStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "status")
    private Long status;

    @Column(name = "result")
    private Long result;

    @Column(name = "detail")
    private String detail;

}
