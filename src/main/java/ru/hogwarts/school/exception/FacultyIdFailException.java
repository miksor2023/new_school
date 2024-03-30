package ru.hogwarts.school.exception;

public class FacultyIdFailException extends IdFailException{
    private final long id;

    public FacultyIdFailException(long id) {
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Факультет с ID %d не найден".formatted(id);
    }
}
