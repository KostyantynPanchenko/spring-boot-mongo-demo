package com.example.mongo.service;

import com.example.mongo.domain.Student;
import com.example.mongo.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository repository;

  public List<Student> findAll() {
    return repository.findAll();
  }
}
