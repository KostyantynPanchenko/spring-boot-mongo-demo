package com.example.mongo.controller;

import com.example.mongo.domain.Student;
import com.example.mongo.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @GetMapping("all")
  public List<Student> getAllStudents() {
    return studentService.findAll();
  }
}
