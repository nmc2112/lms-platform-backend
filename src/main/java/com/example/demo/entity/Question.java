package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {
    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "content")
    private String content;

    @Column(columnDefinition = "json", name = "options")
    private String options;

    @Column(name = "chapterId")
    private String chapterId;

    @Column(name = "correctOption")
    private Integer correctOption;

    @Column(name = "quesCategoryId")
    private String quesCategoryId;


}
