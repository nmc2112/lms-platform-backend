package com.example.demo.service.impl;

import com.example.demo.dto.UserClerkDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


    @Value("${clerk.api.key}")
    private String clerkApiKey;

    @Value("${clerk.api.url}")
    private String clerkApiUrl;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllTeacher() {
        return userRepository.findAllTeacher();
    }

    @Override
    public User save(User user) {
        user.setPassword(this.getRandomString(6));
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void getUsersFromClerk() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clerkApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserClerkDTO[]> response = restTemplate.exchange(clerkApiUrl, HttpMethod.GET, entity, UserClerkDTO[].class);
        List<User> rs = new ArrayList<>();
        userRepository.deleteUsersFromClerk();
        for (UserClerkDTO userClerkDTO : response.getBody()) {
            User user = new User();
            user.setUsername(userClerkDTO.getUsername()==null?userClerkDTO.getEmail_addresses().get(0).getEmail_address():userClerkDTO.getUsername());
            user.setPassword(userClerkDTO.getPassword()==null?"":userClerkDTO.getPassword());
            user.setEmail(userClerkDTO.getEmail_addresses().get(0).getEmail_address());
            user.setName(userClerkDTO.getFirst_name());
            user.setUserClerkId(userClerkDTO.getId());
            rs.add(user);
        }
        userRepository.saveAll(rs);
    }


    private String getRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int charIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(charIndex);
            result.append(randomChar);
        }
        return result.toString();
    }


}
