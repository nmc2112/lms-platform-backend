package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Date;

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
//    @Temporal(TemporalType.DATE)
    private Date startTime;

    @Column(name = "endTime", nullable = true)
//    @Temporal(TemporalType.DATE)
    private Date endTime;

    @Column(name = "meetingLink", length = 255, nullable = true)
    private String meetingLink;

    @Column(name = "hostId", nullable = true)
    private Long hostId;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "imgUrl", nullable = true)
    private String imgUrl;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
