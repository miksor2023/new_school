package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student postStudent(@RequestBody Student student){
        return studentService.postStudent(student);
    }
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id){

        return studentService.getStudent(id);
    }
    @PutMapping
    public Student updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
    @GetMapping
    public List<Student> getStudentsByAge(@RequestParam int age){
        return studentService.findByAge(age);
    }
}
