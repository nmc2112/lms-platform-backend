package com.example.demo.controller;

import com.example.demo.dto.AssignmentStudentRequest;
import com.example.demo.entity.AssignmentStudent;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
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

    @GetMapping("/get-assignment-by-teacherId")
    public ResponseEntity getAllAssignmentByTeacherId(HttpServletRequest request) {
        return ResponseEntity.ok(assignmentService.findAllByTeacherId(request));
    }

    @GetMapping("/get-students-by-assignmentId/{assignmentId}")
    public ResponseEntity getAllStudentsByAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getAllStudentsByAssignmentId(assignmentId));
    }

    @GetMapping("/get-finished-assignment/{assignmentId}")
    public ResponseEntity getFinishedAssignment(HttpServletRequest request, @PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getFinishedAssignment(request, assignmentId));
    }

    @GetMapping("/get-assignment-by-id/{id}")
    public ResponseEntity getAllAssignmentById(@PathVariable("id")  Long id, HttpServletRequest request) {
        return ResponseEntity.ok(assignmentService.findById(id, request));
    }

    @PostMapping("/save-assignment")
    public ResponseEntity saveAssignment(@RequestBody AssignmentStudentRequest assignmentStudent) {
        return ResponseEntity.ok(assignmentService.saveAssignmentByStudent(assignmentStudent));
    }

    @PostMapping("/update-assignmentStudent")
    public ResponseEntity updateAssignmentStudent(@RequestBody AssignmentStudent assignmentStudent, HttpServletRequest request) {
        return ResponseEntity.ok(assignmentService.updateAssignmentStudent(assignmentStudent, request));
    }

}
