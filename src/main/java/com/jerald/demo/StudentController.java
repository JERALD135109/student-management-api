package com.jerald.demo;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        log.info("Fetching student id={}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found, id={}", id);
                    return new StudentNotFoundException(id);
                });
    }

    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        Student saved = studentRepository.save(student);
        log.info("Added student id={} name={}", saved.getId(), saved.getName());
        return saved;
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable int id, @Valid @RequestBody Student updatedStudent) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed, student not found, id={}", id);
                    return new StudentNotFoundException(id);
                });
        s.setName(updatedStudent.getName());
        s.setDepartment(updatedStudent.getDepartment());
        Student saved = studentRepository.save(s);
        log.info("Updated student id={}", id);
        return saved;
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        if (!studentRepository.existsById(id)) {
            log.warn("Delete failed, student not found, id={}", id);
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
        log.info("Deleted student id={}", id);
        return "Deleted student with id " + id;
    }
}
