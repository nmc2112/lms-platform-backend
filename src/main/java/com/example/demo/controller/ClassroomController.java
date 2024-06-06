package com.example.demo.controller;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.impl.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ClassroomController {

    private final ClassroomService classroomService;
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
}
