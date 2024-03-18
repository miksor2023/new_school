package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarView;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarIdFailException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public List<Avatar> findAll() {
       return avatarRepository.findAll();
    }

    public List<Avatar> getAvatarsPaginated(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

//    public AvatarView getAvatarsPaginated(int pageNumber, int pageSize) {
//        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
//        return avatarRepository.findAll(pageRequest);
//    }



}
