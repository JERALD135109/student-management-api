package com.jerald.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController controller;

    @Test
    void getAllStudentsReturnsEveryStudent() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(List.of(
                new Student(1, "Jerald", "CSE"),
                new Student(2, "Priya", "ECE")));

        // Act
        List<Student> result = controller.getAllStudents();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Priya", result.get(1).getName());
    }

    @Test
    void getStudentByIdReturnsStudentWhenFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(new Student(1, "Jerald", "CSE")));

        Student found = controller.getStudentById(1);

        assertEquals("Jerald", found.getName());
    }

    @Test
    void getStudentByIdThrowsWhenMissing() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        StudentNotFoundException ex = assertThrows(
                StudentNotFoundException.class,
                () -> controller.getStudentById(99));

        assertEquals("Student not found with id: 99", ex.getMessage());
    }

    @Test
    void addStudentSavesAndReturnsTheSavedRecord() {
        Student input = new Student(null, "Ravi", "IT");
        when(studentRepository.save(input)).thenReturn(new Student(7, "Ravi", "IT"));

        Student saved = controller.addStudent(input);

        assertEquals(7, saved.getId());
    }

    @Test
    void deleteStudentThrowsWhenMissingAndDoesNotDelete() {
        when(studentRepository.existsById(5)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> controller.deleteStudent(5));

        verify(studentRepository, never()).deleteById(5);
    }
}
