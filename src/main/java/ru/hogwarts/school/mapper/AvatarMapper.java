package ru.hogwarts.school.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;

import static ru.hogwarts.school.controller.AvatarController.BASE_URI;

@Component
public class AvatarMapper {
    @Value("${server.port}")
    private int port;
    public AvatarDto avatarToDto (Avatar avatar){
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setStudentId(avatar.getStudent().getId());
        avatarDto.setStudentName(avatar.getStudent().getName());
        avatarDto.setUrl(
                UriComponentsBuilder.newInstance()
                        .scheme("http")
                        .host("localhost")
                        .port(port)
                        .path(BASE_URI)
                        .pathSegment("avatar-from-db")//pathSegment сам добавит слэш в начале
                        .queryParam("studentId", avatar.getStudent().getId())
                        .build()
                        .toString()
        );
        return avatarDto;
    }
}
