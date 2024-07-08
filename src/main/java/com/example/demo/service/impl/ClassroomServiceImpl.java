package com.example.demo.service.impl;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.ImportClassroomDTO;
import com.example.demo.dto.ImportResponse;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentClassroom;
import com.example.demo.entity.User;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.StudentClassroomRepository;
import com.example.demo.repository.UserRepository;
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
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final GoogleCalendarService googleCalendarService;
    private final StudentClassroomRepository studentClassroomRepository;
//    @Autowired
//    private JavaMailSender mailSender;

    @Override
    public List<ClassroomDTO> findAll() {
        return classroomRepository.findAllAsDTO();
    }

    @Override
    public Classroom save(Classroom classroom) throws Exception {
        if (classroom.getId() == null) {
            Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(classroom.getStartTime()), new DateTime(classroom.getEndTime()), new DateTime(classroom.getUntil()));
            classroom.setMeetingLink(e.getHangoutLink());
            classroom.setTotalStudents(0L);
            classroom.setFirstDay(classroom.getStartTime());
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            DateTime startTimeGMT = googleCalendarService.convertToGMTPlus7(new DateTime(classroom.getStartTime()));
            calendar.setTime(new Date(startTimeGMT.getValue()));
            int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

            // Map Java Calendar day of week to RRULE BYDAY format
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            String byDay = daysOfWeek[dayOfWeek - 1];
            classroom.setDayOfWeek(byDay);

            User teacher = userRepository.findById(classroom.getTeacherId()).orElse(new User());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            this.sendSimpleEmail(teacher.getEmail(), "LMS Education notification for teacher", "You has been assigned to teach: " + classroom.getSubjectName() + ".\nYour classroom meeting link: " + classroom.getMeetingLink() + "\nStart at:" + formatter.format(classroom.getStartTime()) + "\nEnd at:" + formatter.format(classroom.getEndTime()));
            return classroomRepository.save(classroom);
        } else {
            Optional<Classroom> classroomOptional = classroomRepository.findById(classroom.getId());
            if (classroomOptional.isPresent()) {
                //neu thoi gian bat dau hoac ket thuc thay doi -> generate lai link gg meet
                if (classroom.getStartTime() != classroomOptional.get().getStartTime() || classroom.getEndTime() != classroomOptional.get().getEndTime()) {
                    Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(classroom.getStartTime()), new DateTime(classroom.getEndTime()), new DateTime(classroom.getUntil()));
                    classroomOptional.get().setMeetingLink(e.getHangoutLink());
                }
                //set lai thuoc tinh update
//                classroomOptional.get().setTotalStudents(classroomOptional.get().getTotalStudents() + 1);
                classroomOptional.get().setStartTime(classroom.getStartTime());
                classroomOptional.get().setEndTime(classroom.getEndTime());
                classroomOptional.get().setSubjectName(classroom.getSubjectName());
                classroomOptional.get().setTeacherId(classroom.getTeacherId());
                classroomRepository.save(classroomOptional.get());
            }
            return null;
        }

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS
//
//        Session session = Session.getInstance(prop,
//                new ja.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.zoho.com");
        mailSender.setPort(465);  // or 587 for TLS
        mailSender.setUsername("giang.dinh@lifesup.com.vn");
        mailSender.setPassword("ESjYL2shH66S");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");  // Use this for SSL
        // props.put("mail.smtp.starttls.enable", "true");  // Uncomment for TLS
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
//        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.zoho.com");
        props.put("mail.smtp.starttls.enable", "true");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("giang.dinh@lifesup.com.vn");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate() {
        // lay data cho vao danh muc
        List<User> students = userRepository.findByRole("student");
        List<ClassroomDTO> classrooms = classroomRepository.findAllAsDTO();

        try (InputStream is = new ClassPathResource("excel/classroomRegister.xlsx").getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet specificationSheet = workbook.getSheet("Specification");
            // Creating header row
            // Populating the data rows
            int rowNum = 2;
            for (User student : students) {
                Row row = specificationSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getName());
                row.createCell(1).setCellValue(student.getEmail());
            }
            rowNum = 2;
            for (ClassroomDTO c : classrooms) {
                Row row;
                if (rowNum > specificationSheet.getLastRowNum()) {
                    row = specificationSheet.createRow(rowNum);
                } else {
                    row = specificationSheet.getRow(rowNum);
                }
                row.createCell(3).setCellValue(c.getSubjectName());
                row.createCell(4).setCellValue(c.getTeacherName());
                rowNum++;
            }
            // fill xong data specification sheet
            Sheet importDataSheet = workbook.getSheet("Import data");

            DataValidationHelper validationHelper = importDataSheet.getDataValidationHelper();

            // Create data validation for column B (Names)
            DataValidationConstraint nameConstraint = validationHelper.createFormulaListConstraint("Specification!$A$3:$A$" + students.size() + 2);
            CellRangeAddressList nameAddressList = new CellRangeAddressList(1, 50, 1, 1);
            DataValidation nameValidation = validationHelper.createValidation(nameConstraint, nameAddressList);

            // Create data validation for column D (teacher)
            DataValidationConstraint emailConstraint = validationHelper.createFormulaListConstraint("Specification!$D$3:$D$" + classrooms.size() + 2);
            CellRangeAddressList emailAddressList = new CellRangeAddressList(1, 50, 3, 3);
            DataValidation emailValidation = validationHelper.createValidation(emailConstraint, emailAddressList);

            // Add the validations to the sheet
            importDataSheet.addValidationData(nameValidation);
            importDataSheet.addValidationData(emailValidation);


            // Write the output to a file
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

                // Get the file's media type (if necessary)
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                // Prepare response with the file
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students.xlsx\"")
                        .body(resource);
            }
        } catch (Exception e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ClassroomDTO findById(Long id) {
        ClassroomDTO classroomDTO = classroomRepository.findByIdAsDTO(id);
        List<Long> studentIds = studentClassroomRepository.findStudentsByClassroomId(id);
        List<User> students = userRepository.findAllById(studentIds);
        classroomDTO.setListStudents(students);
        return classroomDTO;
    }

    @Override
    public ClassroomDTO addStudentToClassroom(Long classroomId, Long studentId) {
        StudentClassroom studentClassroom = new StudentClassroom();
        studentClassroom.setStudentId(studentId);
        studentClassroom.setClassroomId(classroomId);
        Classroom classroom = classroomRepository.findById(classroomId).orElse(new Classroom());
        User user = userRepository.findById(studentId).orElse(new User());
        classroom.setTotalStudents(classroom.getTotalStudents() + 1);
        classroomRepository.save(classroom);
        studentClassroomRepository.save(studentClassroom);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.sendSimpleEmail(user.getEmail(), "LMS Education notification for student", "You has been added to a new classroom.\nSubject name: " + classroom.getSubjectName() + ".\nYour classroom meeting link: " + classroom.getMeetingLink() + "\nStart at:" + formatter.format(classroom.getStartTime()) + "\nEnd at:" + formatter.format(classroom.getEndTime()));
        return findById(classroomId);
    }

    @Override
    public List<User> getStudentsToAdd(Long classroomId) {
        return List.of();
    }

    @Override
    public ImportResponse importExcel(MultipartFile file) {
        ImportResponse response = new ImportResponse();
        List<String> errors = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 1; //bỏ qua 1 dòng heading
            List<ImportClassroomDTO> rs = new ArrayList<>();
            for (int i = startRow; i < sheet.getLastRowNum(); i++) {
                ImportClassroomDTO importClassroomDTO = new ImportClassroomDTO();
                Row currentRow = sheet.getRow(i);
                if (!isRowNotEmpty(currentRow)) break;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                boolean studentNameIsBlank = false;
                boolean studentEmailIsBlank = false;
                boolean subjectNameIsBlank = false;
                boolean teacherNameIsBlank = false;
                String inputStudentName, inputStudentEmail = "", inputSubjectName = "", inputTeacherName = "";
                for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            String number = currentCell.getStringCellValue().trim();
                            if (!number.isEmpty()&&!Utils.isLong(number)) {
                                errors.add("Line " + (i + 1) + ": Invalid number");
                            }
                            break;
                        case 1:
                            inputStudentName = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(inputStudentName)) {
                                errors.add("Line " + (i + 1) + ": Student name is required");
                                studentNameIsBlank = true;
                            }
                            break;
                        case 2:
                            inputStudentEmail = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(inputStudentEmail)) {
                                errors.add("Line " + (i + 1) + ": Student email is required");
                                studentEmailIsBlank = true;
                            }
                            break;
                        case 3:
                            inputSubjectName = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(inputSubjectName)) {
                                errors.add("Line " + (i + 1) + ": Subject name is required");
                                subjectNameIsBlank = true;
                            }
                            break;
                        case 4:
                            inputTeacherName = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(inputTeacherName)) {
                                errors.add("Line " + (i + 1) + ": Teacher name is required");
                                teacherNameIsBlank = true;
                            }
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (!studentNameIsBlank && !studentEmailIsBlank) {
                    User student = userRepository.findByEmail(inputStudentEmail);
                    if (student == null) {
                        errors.add("Line " + (i + 1) + ": Student not found");
                    } else {
                        importClassroomDTO.setStudent(student);
                    }
                }
                if (!subjectNameIsBlank && !teacherNameIsBlank) {
                    Classroom classroom = classroomRepository.findBySubjectNameAndTeacherName(inputSubjectName,inputTeacherName);
                    if (classroom == null) {
                        errors.add("Line " + (i + 1) + ": Classroom not found");
                    } else {
                        importClassroomDTO.setClassroom(classroom);
                    }
                }
                rs.add(importClassroomDTO);
            }
            if (errors.isEmpty()){
                List<StudentClassroom> studentClassrooms = rs.stream()
                        .map(data -> new StudentClassroom(data.getStudent().getId(), data.getClassroom().getId()))
                        .toList();
                studentClassroomRepository.saveAll(studentClassrooms);
                response.setSuccess(true);
                return response;
            } else {
                response.setSuccess(false);
                response.setError(errors);
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isRowNotEmpty(Row row) {
        if (row == null) {
            return false;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum <= row.getLastCellNum(); cellNum++) {
            if (row.getCell(cellNum) != null && !row.getCell(cellNum).toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<Resource> exportStudentList(long id) {
        ClassroomDTO classroomDTO = classroomRepository.findByIdAsDTO(id);
        List<Long> studentIds = studentClassroomRepository.findStudentsByClassroomId(id);
        List<User> students = userRepository.findAllById(studentIds);
        try (InputStream is = new ClassPathResource("excel/studentListByClassroom.xlsx").getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet specificationSheet = workbook.getSheetAt(0);
            // Creating header row
            // Populating the data rows
            int rowNum = 1;
            for (User student : students) {
                Row row = specificationSheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum++);
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getEmail());
            }

            // Write the output to a file
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

                // Get the file's media type (if necessary)
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                // Prepare response with the file
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students.xlsx\"")
                        .body(resource);
            }
        } catch (Exception e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public List<ClassroomDTO> findAllByTeacherId(HttpServletRequest request) {
        String userId = request.getHeader("Userid");
        return classroomRepository.findAllByTeacherIdAsDTO(userId);

    }

    @Override
    public List<ClassroomDTO> findAllByStudentId(HttpServletRequest request) {
        String userId = request.getHeader("Userid");
        return classroomRepository.findAllByStudentIdAsDTO(userId);
    }
}
