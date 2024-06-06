package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // validate username and password
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // generate and return token
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }

    @GetMapping("/sync")
    public ResponseEntity syncUser() {
        userService.getUsersFromClerk();
        return ResponseEntity.ok("sync complete");
    }

    @GetMapping("/get-all-users")
    public ResponseEntity listAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.findAll());
    }
    // token generation method
}


