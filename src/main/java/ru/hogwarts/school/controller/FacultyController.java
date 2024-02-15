package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty postFaculty(@RequestBody Faculty faculty){
        return facultyService.postFaculty(faculty);
    }
    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id){
        return facultyService.getFaculty(id);
    }
    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty){
        return facultyService.updateFaculty(faculty);
    }
    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable Long id){ return facultyService.deleteFaculty(id); }
    @GetMapping
    public List<Faculty> getFacultiesByColor(@RequestParam String color){
        return facultyService.getFacultiesByColor(color);
    }
}
