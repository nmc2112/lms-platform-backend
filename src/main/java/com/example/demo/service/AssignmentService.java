package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.AssignmentStudent;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    public List<AssignmentDTO> findAllByStudentId(HttpServletRequest request);

    public AssignmentStudentDTO findById(Long id, HttpServletRequest request);

    public AssignmentStudent saveAssignmentByStudent(AssignmentStudentRequest assignmentStudent);

}
