package com.example.demo.dto;

import com.example.demo.entity.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignmentStudentDTO {
    private Long duration;
    private Long totalQuestions;
    private List<Question> questions;

    private String detail;

}
