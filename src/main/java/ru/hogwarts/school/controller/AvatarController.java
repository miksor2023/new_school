package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.service.AvatarServise;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(AvatarController.BASE_URI)
public class AvatarController {
    public static final String BASE_URI = "/avatar";
    private AvatarServise avatarServise;

    public AvatarController(AvatarServise avatarServise) {
        this.avatarServise = avatarServise;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestParam Long studentId, @RequestPart MultipartFile avatar) throws IOException {
        return ResponseEntity.ok(avatarServise.uploadAvatar(studentId, avatar));
    }

    @GetMapping(params = {"page", "size"})
    @Operation(summary = "get avatars paginated")
    public List<AvatarDto> getAvatarsPaginated(@RequestParam int page, @RequestParam int size){
        return avatarServise.getAvatarsPaginated(page, size);
    }

    @GetMapping(value = "/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@RequestParam Long studentId) {
        return transform(avatarServise.findAvatarFromDb(studentId));
    }

    @GetMapping(value = "/avatar-from-file")
    public ResponseEntity<byte[]> downloadAvatarFromFile(@RequestParam Long studentId) throws IOException {
        return transform(avatarServise.findAvatarFromFile(studentId));
    }

    public ResponseEntity<byte[]> transform(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        ;
        return ResponseEntity.ok()
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }

}
