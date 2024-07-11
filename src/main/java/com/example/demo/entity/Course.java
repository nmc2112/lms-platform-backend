package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "price")
    private Double price;

    @Column(name = "isPublished")
    private Long isPublished;

    @Column(name = "categoryId")
    private String categoryId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updateAt")
    private Date updateAt;
}
