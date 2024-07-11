package com.example.demo.service.impl;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.ImportClassroomDTO;
import com.example.demo.dto.ImportResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.CourseService;
import com.example.demo.util.Utils;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;


    @Override
    public List<CourseDTO> findAllCourseByTeacherId(HttpServletRequest request) {
        String userId = request.getHeader("Userid");
        return courseRepository.findAllCourseByTeacherIdAsDTO(userId);
    }

    @Override
    public List<CourseDTO> findAll() {
        return courseRepository.findAllAsDTO();
    }
}
//@Override
//public List<ClassroomDTO> findAll() {
//    return classroomRepository.findAllAsDTO();
//}