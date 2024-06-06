package com.example.demo.service;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;

import java.util.List;

public interface ClassroomService {
    public List<ClassroomDTO> findAll();
    public Classroom save(Classroom classroom) throws Exception;
    public void delete(Long id);

    void sendSimpleEmail(String to, String subject, String text);
}
