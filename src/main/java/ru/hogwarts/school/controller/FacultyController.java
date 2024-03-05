package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> postFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.postFaculty(faculty));
    }

    @GetMapping
    @Operation(summary = "Get all faculties")
    public List<Faculty> findAll() {
        return facultyService.findAll();
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }


    @GetMapping(params = "color")
    @Operation(summary = "Get faculties by color")
    public List<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.findByColor(color);
    }

    @GetMapping(params = "nameOrColor")
    @Operation(summary = "Get faculties by name or color")
    public List<Faculty> getFacultiesByNameOrColorIgnoreCase(@RequestParam String nameOrColor) {
        return facultyService.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor);
    }

    @GetMapping("/{id}/students")
    @Operation(summary = "Get students of faculty")
    public List<Student> getStudents(@PathVariable Long id) {
        return facultyService.findStudentsByFaculty_Id(id);
    }


    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok("Факультет с ID %d удалён".formatted(id));
    }


}
