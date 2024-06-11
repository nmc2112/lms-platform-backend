package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "student_classroom")
@Getter
@Setter
@NoArgsConstructor
public class StudentClassroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "studentId")
    private Long studentId;

    @Column(name = "classroomId")
    private Long classroomId;

    public StudentClassroom(Long studentId, Long classroomId) {
        this.studentId = studentId;
        this.classroomId = classroomId;
    }
}
