package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/port")
public class InfoController {
    @Value("${server.port}")
    private int port;
    @GetMapping
    @Operation(summary = "get server port")
    public ResponseEntity<String> getServerPort(){
        return ResponseEntity.ok("Server port: %d".formatted(port));
    }


}
