package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class IncorrectIdException extends RuntimeException{
    public IncorrectIdException() {
    }

    public IncorrectIdException(String message) {
        super(message);
    }

    public IncorrectIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectIdException(Throwable cause) {
        super(cause);
    }

    public IncorrectIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
