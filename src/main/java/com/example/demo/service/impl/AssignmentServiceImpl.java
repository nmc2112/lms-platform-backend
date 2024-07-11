package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import com.example.demo.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentStudentRepository assignmentStudentRepository;
    private final UserRepository userRepository;
    private final GoogleCalendarService googleCalendarService;
    private final StudentClassroomRepository studentClassroomRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<AssignmentDTO> findAllByStudentId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        List<Long> classroomIds = studentClassroomRepository.findClassroomsByStudentId(userId);
        List<AssignmentDTO> assignmentDTOList =  assignmentRepository.findAllByClassroomIds(classroomIds);
        assignmentDTOList.forEach(assignmentDTO -> {
            AssignmentStudent assignmentStudent = assignmentStudentRepository.findByAssignmentIdAndStudentId(assignmentDTO.getId(), userId);
            if(assignmentStudent != null) {
                assignmentDTO.setResult(assignmentStudent.getResult());
                assignmentDTO.setStatus(assignmentStudent.getStatus());
                assignmentDTO.setDetail(assignmentStudent.getDetail());
            }
            else{
                assignmentDTO.setStatus(0L);
            }
        });
        return assignmentDTOList;
    }

    @Override
    public List<AssignmentDTO> findAllByTeacherId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        return assignmentRepository.findAllByTeacherId(userId);
    }

    @Override
    public AssignmentStudentDTO findById(Long id, HttpServletRequest request) {
        AssignmentStudentDTO assignmentStudentDTO = new AssignmentStudentDTO();
        Assignment assignment = assignmentRepository.findById(id).orElse(null);
        assignmentStudentDTO.setQuestions(questionRepository.findQuestionsByAssignment(assignment.getId(), assignment.getNumberOfQuestions()));
        assignmentStudentDTO.setDuration(assignment.getDuration());
        assignmentStudentDTO.setTotalQuestions(assignment.getNumberOfQuestions());

        return assignmentStudentDTO;
    }

    @Override
    public AssignmentStudent saveAssignmentByStudent(AssignmentStudentRequest request) {
        AssignmentStudent assignmentStudent = new AssignmentStudent();
        assignmentStudent.setAssignmentId(request.getAssignmentId());
        assignmentStudent.setStatus(1L);
        assignmentStudent.setResult(request.getResult());
        assignmentStudent.setDetail(request.getDetail());

        User user = userRepository.findByUserClerkId(request.getStudentId());
        assignmentStudent.setStudentId(user.getId());

        return assignmentStudentRepository.save(assignmentStudent);
    }

    @Override
    public AssignmentStudent updateAssignmentStudent(AssignmentStudent assignmentStudent, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        AssignmentStudent existingAssignmentStudent = assignmentStudentRepository.findByAssignmentIdAndStudentId(assignmentStudent.getAssignmentId(), userId);

        existingAssignmentStudent.setTeacherComment(assignmentStudent.getTeacherComment());
        // Save the updated assignment student record back to the repository
        return assignmentStudentRepository.save(existingAssignmentStudent);

    }


    @Override
    public AssignmentStudentDTO getFinishedAssignment(HttpServletRequest request, Long assignmentId) {
        String userId = request.getHeader("userId");

        AssignmentStudent assignmentStudent =  assignmentStudentRepository.findByAssignmentIdAndStudentId(assignmentId,userId);

        AssignmentStudentDTO assignmentStudentDTO = new AssignmentStudentDTO();

        List<AnswerDTO> answerDTOS = this.parseJsonToDto(assignmentStudent.getDetail());
        assignmentStudentDTO.setDetail(answerDTOS);
        assignmentStudentDTO.setStatus(assignmentStudent.getStatus());
        assignmentStudentDTO.setTeacherComment(assignmentStudent.getTeacherComment());
        List<String> questionIds = answerDTOS.stream().map(AnswerDTO::getQuestionId).collect(Collectors.toList());
        List<Question> questions = questionRepository.findAllById(questionIds);
        assignmentStudentDTO.setQuestions(questions);
        return assignmentStudentDTO;
    }

    @Override
    public List<AssignmentByStudentResponse> getAllStudentsByAssignmentId(Long assignmentId) {
        List<AssignmentByStudentResponse> assignmentByStudentResponseList = new ArrayList<>();
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        List<Long> studentIds = studentClassroomRepository.findStudentsByClassroomId(assignment.getClassroomId());
        List<User> students = userRepository.findAllById(studentIds);

        students.forEach(student -> {
            AssignmentByStudentResponse assignmentByStudentResponse = new AssignmentByStudentResponse();
            AssignmentStudent assignmentStudent = assignmentStudentRepository.findByAssignmentIdAndStudentId(assignment.getId(), student.getUserClerkId());
            if (assignmentStudent == null) {
                assignmentByStudentResponse.setStudentId(student.getUserClerkId());
                assignmentByStudentResponse.setStudentEmail(student.getEmail());
                assignmentByStudentResponse.setStudentName(student.getName());
                assignmentByStudentResponse.setAssignmentId(assignment.getId());
                assignmentByStudentResponse.setStatus(0L);
            } else {
                assignmentByStudentResponse.setStudentId(student.getUserClerkId());
                assignmentByStudentResponse.setStudentEmail(student.getEmail());
                assignmentByStudentResponse.setStudentName(student.getName());
                assignmentByStudentResponse.setStatus(1L);
                assignmentByStudentResponse.setResult(assignmentStudent.getResult());
                assignmentByStudentResponse.setAssignmentId(assignment.getId());
            }
            assignmentByStudentResponseList.add(assignmentByStudentResponse);
        });
        return assignmentByStudentResponseList;
    }

    private List<AnswerDTO> parseJsonToDto(String jsonArray) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<AnswerDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }//    @Autowired
//    private JavaMailSender mailSender;



}
