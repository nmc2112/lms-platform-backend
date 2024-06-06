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
public class ClassroomDTO {
    private Long id;

    private String subjectName;

    private Long totalStudents;

    private Long teacherId;
    private String teacherName;

    private Date startTime;

    private Date endTime;
    private String meetingLink;
    private String description;
    private String imgUrl;
    private List<User> listStudents;

    public ClassroomDTO() {
    }

    public ClassroomDTO(Long id, String subjectName, Long totalStudents, Long teacherId,  String teacherName, Timestamp startTime, Timestamp endTime, String meetingLink, String description, String imgUrl) {
        this.id = id;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.totalStudents = totalStudents;
        this.startTime = startTime != null ? new Date(startTime.getTime()) : null;
        this.endTime = endTime != null ? new Date(endTime.getTime()) : null;
        this.meetingLink = meetingLink;
        this.description = description;
        this.imgUrl = imgUrl;
    }
}
