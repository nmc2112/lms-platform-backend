package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserClerkDTO {
    private String id;
    private String username;
    private String password;
    private List<Email> email_addresses;
}
