package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

}
