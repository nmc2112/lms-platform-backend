package com.example.demo.controller;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.impl.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/get-assignment-by-studentId")
    public ResponseEntity getAllAssignmentByStudentId(HttpServletRequest request) {
        return ResponseEntity.ok(assignmentService.findAllByStudentId(request));
    }

}
