package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "classroom")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subjectName", length = 100, nullable = true)
    private String subjectName;

    @Column(name = "totalStudents", nullable = true)
    private Long totalStudents;

    @Column(name = "teacherId", nullable = true)
    private Long teacherId;

    @Column(name = "startTime", nullable = true)
    private Date startTime;

    @Column(name = "endTime", nullable = true)
    private Date endTime;

    @Column(name = "firstDay", nullable = true)
    private Date firstDay;

    @Column(name = "until", nullable = true)
    private Date until;

    @Column(name = "dayOfWeek", nullable = true)
    private String dayOfWeek;

    @Column(name = "meetingLink", length = 255, nullable = true)
    private String meetingLink;

    @Column(name = "hostId", nullable = true)
    private Long hostId;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "imgUrl", nullable = true)
    private String imgUrl;

    // Getters and Setters


}
