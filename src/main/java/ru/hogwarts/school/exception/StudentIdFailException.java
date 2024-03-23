package ru.hogwarts.school.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentIdFailException extends IdFailException{
    Logger logger = LoggerFactory.getLogger(StudentIdFailException.class);
    private final long id;

    public StudentIdFailException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        logger.debug("Run getMessage method in StudentIdFailException.class");
        return "Студент с ID %d не найден".formatted(id);
    }
}
