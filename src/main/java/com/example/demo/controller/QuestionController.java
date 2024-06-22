package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class QuestionController {
    private final QuestionService questionService;

//    @GetMapping("/get-all-users")
//    public ResponseEntity listAllUsers() {
//        return ResponseEntity.ok(questionService.findAll());
//    }

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) {

        return questionService.downloadTemplate(request);
    }

    @PostMapping("/import")
    public ResponseEntity importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return ResponseEntity.ok(questionService.importExcel(file, request));
    }
}


