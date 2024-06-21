package com.example.demo.service;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.ImportResponse;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {
    ResponseEntity<Resource> downloadTemplate(HttpServletRequest request);
//
//    public ImportResponse importExcel(MultipartFile file);
//
//    ResponseEntity<Resource> exportStudentList(long id);
}
