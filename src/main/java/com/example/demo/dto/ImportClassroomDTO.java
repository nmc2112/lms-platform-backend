package com.example.demo.dto;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImportClassroomDTO {
    User student;
    Classroom classroom;
}
