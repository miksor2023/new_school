package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.MathService;

import java.util.stream.Stream;

@RestController
@RequestMapping("/math")
public class MathController {
    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("option-one")
    public ResponseEntity<String> clculateMathExpressionProposed() {
        String result = MathService.clculateMathExpressionProposed();
        return ResponseEntity.ok(result);
    }
    @GetMapping("option-two")
    public ResponseEntity<String> clculateMathExpressionModified() {
        String result = MathService.clculateMathExpressionModified();
        return ResponseEntity.ok(result);
    }
}
