package com.example.demo.controller;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.GoogleCalendarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;


    @GetMapping("/get-all-courses")
    public ResponseEntity getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/get-all-courses-by-teacherId")
    public ResponseEntity getAllCoursesByTeacherId(HttpServletRequest request) {
        return ResponseEntity.ok(courseService.findAllCourseByTeacherId(request));
    }
}
