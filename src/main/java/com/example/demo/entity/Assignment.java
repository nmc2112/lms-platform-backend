package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "assignment")
@Getter
@Setter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "numberOfQuestions")
    private Long numberOfQuestions;

    @Column(name = "userId")
    private String userId;

    @Column(name = "classroomId")
    private Long classroomId;

    @Column(name = "quesCategoryId")
    private String quesCategoryId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;
}
