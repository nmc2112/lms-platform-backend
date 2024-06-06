package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    public User findByUsername(String username);
    public List<User> findAll();
    public User save(User user);
    public void delete(Long id);
    public void getUsersFromClerk();
    public List<User> findAllTeacher();
}
