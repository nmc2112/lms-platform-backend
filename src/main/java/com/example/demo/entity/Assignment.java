package com.example.demo.entity;

import javax.persistence.*;
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
    private int duration;

    @Column(name = "numberOfQuestions")
    private int numberOfQuestions;

    @Column(name = "userId")
    private String userId;

    @Column(name = "classroomId")
    private String classroomId;

    @Column(name = "quesCategoryId")
    private String quesCategoryId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;
}
