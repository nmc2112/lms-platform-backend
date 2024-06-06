package com.example.demo.controller;

import com.example.demo.entity.Classroom;

import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.impl.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomRepository classroomRepository;
    private final GoogleCalendarService googleCalendarService;


    @GetMapping("/get-all")
    public ResponseEntity getAllClassrooms() {
        return ResponseEntity.ok(classroomService.findAll());
    }


    @PostMapping("/save")
    public ResponseEntity getAllClassrooms(@RequestBody Classroom classroom) throws Exception {
        // id=null -> create ---- id!=null -> update
        return ResponseEntity.ok(classroomService.save(classroom));
    }

    @GetMapping("/createGoogleMeet")
    public String createGoogleMeet() {
        try {
            Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(System.currentTimeMillis()),new DateTime(System.currentTimeMillis()+3600000));
            return "Google Meet link created: " + e.getHangoutLink();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create event.";
        }
    }

    @GetMapping("/sendEmail")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            classroomService.sendSimpleEmail(to, subject, text);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email";
        }
    }

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate() {
        return classroomService.downloadTemplate();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.findById(id));
    }

    @PostMapping("/add-student-to-classroom")
    public ResponseEntity addStudentToClassroom(@RequestBody Map<String, Long> map) {
        Long studentId = map.get("studentId");
        Long classroomId = map.get("classroomId");
        return ResponseEntity.ok(classroomService.addStudentToClassroom(classroomId,studentId));
    }
}
