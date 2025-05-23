package com.example.demo.controller;

import com.example.demo.entity.Classroom;

import com.example.demo.entity.StudentClassroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

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
    private final UserService userService;


    @GetMapping("/get-all")
    public ResponseEntity getAllClassrooms() {
        return ResponseEntity.ok(classroomService.findAll());
    }

    @GetMapping("/{classroomId}/get-students-to-add")
    public ResponseEntity listAllStudentsToAdd(@PathVariable Long classroomId) {
        return ResponseEntity.ok(classroomService.listAllStudentsToAdd(classroomId));
    }

    @GetMapping("/get-all-by-teacherId")
    public ResponseEntity getAllClassroomsByTeacherId(HttpServletRequest request) {
        return ResponseEntity.ok(classroomService.findAllByTeacherId(request));
    }

    @GetMapping("/get-all-by-studentId")
    public ResponseEntity getAllClassroomsByStudentId(HttpServletRequest request) {
        return ResponseEntity.ok(classroomService.findAllByStudentId(request));
    }


    @PostMapping("/save")
    public ResponseEntity getAllClassrooms(@RequestBody Classroom classroom) throws Exception {
        // id=null -> create ---- id!=null -> update
        return ResponseEntity.ok(classroomService.save(classroom));
    }

    @PostMapping("/delete-student")
    public ResponseEntity deleteStudentInClassroom(@RequestBody StudentClassroom studentClassroom) {
        classroomService.deleteStudentInClassroom(studentClassroom);
        return ResponseEntity.ok("Deleted student with id " + studentClassroom.getStudentId());
    }

//    @GetMapping("/createGoogleMeet")
//    public String createGoogleMeet() {
//        try {
//            Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(System.currentTimeMillis()),new DateTime(System.currentTimeMillis()+3600000));
//            return "Google Meet link created: " + e.getHangoutLink();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to create event.";
//        }
//    }

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


    @GetMapping("/exportStudentListByClass/{id}")
    public ResponseEntity<Resource> exportStudentListByClass(@PathVariable long id) {
        return classroomService.exportStudentList(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.findById(id));
    }

    @GetMapping("/get-students-to-add/{classroomId}")
    public ResponseEntity getStudentsToAdd(@PathVariable Long classroomId) {
        return ResponseEntity.ok(classroomService.getStudentsToAdd(classroomId));
    }

    @PostMapping("/add-student-to-classroom")
    public ResponseEntity addStudentToClassroom(@RequestBody Map<String, Long> map) {
        Long studentId = map.get("studentId");
        Long classroomId = map.get("classroomId");
        return ResponseEntity.ok(classroomService.addStudentToClassroom(classroomId,studentId));
    }

    @PostMapping("/import")
    public ResponseEntity importExcel(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(classroomService.importExcel(file));
    }
}
