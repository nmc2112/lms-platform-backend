package com.example.demo.dto;

import com.example.demo.entity.User;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CourseDTO {
    private String id;

    private String userId;
    private String username;

    private String title;

    private String description;

    private String imageUrl;

    private Double price;

    private Long isPublished;

    private String categoryId;
    private String categoryName;

    private Date createdAt;

    private Date updateAt;

    public CourseDTO() {
    }

    public CourseDTO(String id, String userId, String username, String title, String description, String imageUrl, Double price, Long isPublished, String categoryId, String categoryName, Date createdAt, Date updateAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isPublished = isPublished;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
