package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class MathService {
    public static String clculateMathExpressionProposed() {
        long startTime = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1L).limit(1_000_000_0L).reduce(0L, (a, b) -> a + b);
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        String result = "Proposed: Результат вычислений: %d, затрачено времени: %d мс".formatted(sum, timeElapsed);
        return result;
    }

    public static String clculateMathExpressionModified() {
        long startTime = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1L).parallel().limit(1_000_000_0L).reduce(0L, (a, b) -> a + b);
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        String result = "Modified: Результат вычислений: %d, затрачено времени: %d мс".formatted(sum, timeElapsed);
        return result;
    }
}
