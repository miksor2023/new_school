package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student postStudent(@RequestBody Student student) {
        return studentService.postStudent(student);
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }


    @GetMapping(value = "/age",params = "age")
    @Operation(summary = "Get students by age")
    public List<Student> getStudentsByAge(@RequestParam Integer age) {
        return studentService.findByAge(age);
    }

    @GetMapping(value = "/age/between", params = {"lowerAge, upperAge"})
    @Operation(summary = "Get students by age between")
    public List<Student> getStudentsByAgeBetween(@RequestParam Integer lowerAge, @RequestParam Integer upperAge) {
        return studentService.findByAgeBetween(lowerAge, upperAge);
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("{id}/faculty")
    public Faculty getFacultyOfStudent(@PathVariable Long id) {
        return studentService.getFaculty(id);
    }


    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

}
