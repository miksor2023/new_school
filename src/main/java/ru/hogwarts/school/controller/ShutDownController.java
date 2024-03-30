package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.entity.Student;

import java.util.List;

@RestController
@RequestMapping("/off")
public class ShutDownController {
    @GetMapping
    @Operation(summary = "Shut the application down")
    public ResponseEntity<String> shutDown() {
        System.exit(0);
        return ResponseEntity.ok("");
    }
}
