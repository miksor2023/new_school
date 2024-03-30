package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Student> postStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.postStudent(student));
    }
    @GetMapping("/qty")
    @Operation(summary = "get quantity of students")
    public ResponseEntity<String> getStudentsQty(){
        return ResponseEntity.ok("Количество студентов: %d".formatted(studentService.getStudensQty()));
    }
    @GetMapping("/average-age")
    @Operation(summary = "get average age of students")
    public ResponseEntity<String> getStudentsAverageAge(){
        return ResponseEntity.ok("Средний возраст студентов: %.2f".formatted(studentService.getStudensAverageAge()));
    }
    @GetMapping("/last-five-students")
    @Operation(summary = "get list of last five students")
    public List<Student> getLastFive(){
        return studentService.getLastFive();
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }


    @GetMapping(params = "age")
    @Operation(summary = "Get students by age")
    public List<Student> getStudentsByAge(@RequestParam Integer age) {
        return studentService.findByAge(age);
    }

    @GetMapping(params = {"lowerAge", "upperAge"})
    @Operation(summary = "Get students by age between")
    public List<Student> getStudentsByAgeBetween(@RequestParam int lowerAge, @RequestParam int upperAge) {
        return studentService.findByAgeBetween(lowerAge, upperAge);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get student by ID")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("{id}/faculty")
    @Operation(summary = "Get faculty of student")
    public Faculty getFacultyOfStudent(@PathVariable Long id) {
        return studentService.getFaculty(id);
    }
    @GetMapping("/names")
    @Operation(summary = "Get list of names starts with A")
    public List<String> getNamesStartsWithA(){
        return studentService.getNamesStartsWithA();
    }


    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Студент с ID %d удалён".formatted(id));
    }

}
