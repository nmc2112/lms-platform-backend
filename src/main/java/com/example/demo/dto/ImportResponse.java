package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImportResponse {
    private String message;
    private boolean success;
    private List<String> error;
}
