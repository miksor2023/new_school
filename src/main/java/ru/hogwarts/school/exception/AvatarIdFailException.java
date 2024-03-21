package ru.hogwarts.school.exception;

public class AvatarIdFailException extends IdFailException{
    private final long id;

    public AvatarIdFailException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Аватар для студента с ID %d не найден".formatted(id);
    }
}
