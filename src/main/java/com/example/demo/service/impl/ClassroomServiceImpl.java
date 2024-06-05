package com.example.demo.service.impl;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<Classroom> findAll() {
        return List.of();
    }

    @Override
    public Classroom save(Classroom user) {
        return null;
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
