package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

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

    public ClassroomDTO() {
    }

    public ClassroomDTO(Long id, String subjectName, Long totalStudents, Long teacherId,  String teacherName, Timestamp startTime, Timestamp endTime, String meetingLink) {
        this.id = id;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.totalStudents = totalStudents;
        this.startTime = startTime != null ? new Date(startTime.getTime()) : null;
        this.endTime = endTime != null ? new Date(endTime.getTime()) : null;
        this.meetingLink = meetingLink;
    }
}
