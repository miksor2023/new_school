package ru.hogwarts.school.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.school.service.AvatarServise;

public class AvatarIdFailException extends IdFailException{
    Logger logger = LoggerFactory.getLogger(AvatarIdFailException.class);
    private final long id;

    public AvatarIdFailException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        logger.debug("Run getMessage method in AvatarIdFailException.class");
        return "Аватар для студента с ID %d не найден".formatted(id);
    }
}
