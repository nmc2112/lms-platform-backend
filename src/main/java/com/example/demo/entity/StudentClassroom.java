package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_classroom")
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
