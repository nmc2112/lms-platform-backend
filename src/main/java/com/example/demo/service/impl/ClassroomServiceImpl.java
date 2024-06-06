package com.example.demo.service.impl;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final GoogleCalendarService googleCalendarService;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<ClassroomDTO> findAll() {
        List<ClassroomDTO> classrooms = classroomRepository.findAllAsDTO();
        return classrooms;
    }

    @Override
    public Classroom save(Classroom classroom) throws Exception {
        if (classroom.getId() == null) {
            Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(classroom.getStartTime()), new DateTime(classroom.getEndTime()));
            classroom.setMeetingLink(e.getHangoutLink());
            classroom.setTotalStudents(0l);
            return classroomRepository.save(classroom);
        } else {
            Optional<Classroom> classroomOptional = classroomRepository.findById(classroom.getId());
            if (classroomOptional.isPresent()) {
                //neu thoi gian bat dau hoac ket thuc thay doi -> generate lai link gg meet
                if (classroom.getStartTime()!=classroomOptional.get().getStartTime() || classroom.getEndTime()!=classroomOptional.get().getEndTime()) {
                    Event e = googleCalendarService.createGoogleMeetEvent(new DateTime(classroom.getStartTime()), new DateTime(classroom.getEndTime()));
                    classroomOptional.get().setMeetingLink(e.getHangoutLink());
                }
                //set lai thuoc tinh update
                classroomOptional.get().setTotalStudents(classroomOptional.get().getTotalStudents() + 1);
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
}
