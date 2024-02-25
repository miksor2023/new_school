package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    public Faculty postFaculty(@RequestBody Faculty faculty) {
        return facultyService.postFaculty(faculty);
    }

    @GetMapping
    @Operation(summary = "Get all faculties")
    public List<Faculty> findAll() {
        return facultyService.findAll();
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

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudents(@RequestParam Long id) {
        return facultyService.findByFaculty_Id(id);
    }


    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }


    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }


}
