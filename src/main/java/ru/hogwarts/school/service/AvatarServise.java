package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarIdFailException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServise {
    @Value("${path.to.avatars.folder}")
    private String avatarDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarServise(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }




    public String uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.getStudent(studentId);//меняем на getStudent для проверки на существующий
        Path filePath = Path.of(avatarDir, student.getName() + "_" + "id" + studentId + "." + StringUtils.getFilenameExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        byte[] avatarData = avatarFile.getBytes();
        Files.write(filePath, avatarData);

//        try (
//                InputStream is = avatarFile.getInputStream();
//                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
//                BufferedInputStream bis = new BufferedInputStream(is, 1024);
//                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
//        ) {
//            bis.transferTo(bos);
//        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarData);
        avatarRepository.save(avatar);
        return filePath.toString();
    }

    public Avatar findAvatarBySudentId(Long studentId){
        Student student = studentService.getStudent(studentId);//если нет студента с введённым id, бросит исключение
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> new AvatarIdFailException(studentId));
    }
    public Pair<byte[], String> findAvatarFromDb(long studentId){
        Avatar avatar = findAvatarBySudentId(studentId);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }
    public Pair<byte[], String> findAvatarFromFile(long studentId) throws IOException{
        Avatar avatar = findAvatarBySudentId(studentId);
        byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
        return Pair.of(data, avatar.getMediaType());
    }

}
