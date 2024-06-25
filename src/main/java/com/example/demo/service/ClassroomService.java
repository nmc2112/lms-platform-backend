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

public interface ClassroomService {
    public List<ClassroomDTO> findAll();
    public Classroom save(Classroom classroom) throws Exception;
    public void delete(Long id);

    void sendSimpleEmail(String to, String subject, String text);

    ResponseEntity<Resource> downloadTemplate();
    public ClassroomDTO findById(Long id);
    public ClassroomDTO addStudentToClassroom(Long classroomId, Long studentId);
    List<User> getStudentsToAdd(Long classroomId);

    public ImportResponse importExcel(MultipartFile file);

    ResponseEntity<Resource> exportStudentList(long id);

    public List<ClassroomDTO> findAllByTeacherId(HttpServletRequest request);
}
