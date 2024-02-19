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
    public Student postStudent(@RequestBody Student student){
        return studentService.postStudent(student);
    }
    @GetMapping("/student-by-id/{id}")
    public Student getStudent(@PathVariable Long id){
        return studentService.getStudent(id);
    }
    @GetMapping("/faculty-of-student/{id}")
    public Faculty getFacultyOfStudent(@PathVariable Long id){
        return studentService.getFaculty(id);
    }
    @GetMapping("/students-by-age")
    public List<Student> getStudentsByAge(@RequestParam(required = false) Integer age, @RequestParam(required = false) Integer upperAge){
        if(age != null && upperAge != null){
            return studentService.findByAgeBetween(age, upperAge);
        }
        if(age != null){
            return studentService.findByAge(age);
        }
        return studentService.findAll();
    }
    @PutMapping
    public Student updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }

}
