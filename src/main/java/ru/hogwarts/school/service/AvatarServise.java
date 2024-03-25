package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarIdFailException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvatarServise {
    Logger logger = LoggerFactory.getLogger(AvatarServise.class);
    @Value("${path.to.avatars.folder}")
    private String avatarDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    private final AvatarMapper avatarMapper;

    public AvatarServise(AvatarRepository avatarRepository, StudentService studentService, AvatarMapper avatarMapper) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        this.avatarMapper = avatarMapper;
    }




    public String uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Upload Avatar method was invoked");
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
        logger.info("Find Avatar by student's ID method was invoked");
        Student student = studentService.getStudent(studentId);//если нет студента с введённым id, бросит исключение
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> {
            logger.error("There is no avatar for student with ID = " + studentId);
            return new AvatarIdFailException(studentId);
        });
    }
    public Pair<byte[], String> findAvatarFromDb(long studentId){
        logger.info("Find Avatar from DB method was invoked");
        Avatar avatar = findAvatarBySudentId(studentId);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }
    public Pair<byte[], String> findAvatarFromFile(long studentId) throws IOException{
        logger.info("Find Avatar from Filesystem method was invoked");
        Avatar avatar = findAvatarBySudentId(studentId);
        byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
        return Pair.of(data, avatar.getMediaType());
    }

    public List<AvatarDto> getAvatarsPaginated(int pageNumber, int pageSize) {
        logger.info("Get Avatars by pages method was invoked. Page number: " + pageNumber + ", Page size: " + pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(avatar -> avatarMapper.avatarToDto(avatar))
                .collect(Collectors.toList());
    }



}
