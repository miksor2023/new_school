package ru.hogwarts.school.exception;

public class StudentIdFailException extends IdFailException{
    private final long id;

    public StudentIdFailException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Студент с ID %d не найден".formatted(id);
    }
}
