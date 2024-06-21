package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "quesCategory")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "options")
    private String options;

    @Column(name = "chapterId")
    private UUID chapterId;

    @Column(name = "correctOption")
    private Integer correctOption;

    @Column(name = "quesCategoryId")
    private UUID quesCategoryId;


}
