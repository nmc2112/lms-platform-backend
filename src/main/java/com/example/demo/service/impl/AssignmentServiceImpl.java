package com.example.demo.service.impl;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.ImportClassroomDTO;
import com.example.demo.dto.ImportResponse;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentClassroom;
import com.example.demo.entity.User;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.StudentClassroomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import com.example.demo.util.Utils;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final GoogleCalendarService googleCalendarService;
    private final StudentClassroomRepository studentClassroomRepository;

    @Override
    public List<AssignmentDTO> findAllByStudentId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        List<Long> classroomIds = studentClassroomRepository.findClassroomsByStudentId(userId);
        return assignmentRepository.findAllByClassroomIds(classroomIds);
    }
//    @Autowired
//    private JavaMailSender mailSender;


}
