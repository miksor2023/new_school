package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.service.AvatarServise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private AvatarServise avatarServise;

    public AvatarController(AvatarServise avatarServise) {
        this.avatarServise = avatarServise;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestParam Long studentId, @RequestPart MultipartFile avatar) throws IOException {
        return ResponseEntity.ok(avatarServise.uploadAvatar(studentId, avatar));
    }
    @GetMapping(value = "/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@RequestParam Long studentId) {
        return transform(avatarServise.findAvatarFromDb(studentId));
//        Avatar avatar = avatarServise.findAvatarBySudentId(studentId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
//        headers.setContentLength(avatar.getData().length);
//        return ResponseEntity.status(HttpStatus.OK)
//                .headers(headers)
//                .body(avatar.getData());
    }
    @GetMapping(value = "/avatar-from-file")
    public ResponseEntity<byte[]> downloadAvatarFromFile(@RequestParam Long studentId /*, HttpServletResponse response*/) throws IOException{
        return transform(avatarServise.findAvatarFromFile(studentId));
//        Avatar avatar = avatarServise.findAvatarBySudentId(studentId);
//        Path path = Path.of(avatar.getFilePath());
//        try(InputStream is = Files.newInputStream(path);
//            OutputStream os = response.getOutputStream();) {
//            response.setStatus(200);
//            response.setContentType(avatar.getMediaType());
//            response.setContentLength((int) avatar.getFileSize());
//            is.transferTo(os);
    }
    public ResponseEntity<byte[]> transform (Pair<byte[], String> pair){
        byte[] data = pair.getFirst();;
        return ResponseEntity.ok()
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }

}
