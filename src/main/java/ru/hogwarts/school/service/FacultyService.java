package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    public final StudentRepository studentRepository;



    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty postFaculty(Faculty faculty){
        logger.info("Post faculty method was invoked");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id){
        logger.info("Find faculty by ID method was invoked");
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with ID = " + id);
            return new FacultyIdFailException(id);
        });
    }
    public Faculty updateFaculty(Faculty faculty){
        logger.info("Update faculty method was invoked");
        if(!facultyRepository.existsById(faculty.getId())) {
            logger.error("There is not faculty with ID = " + faculty.getId());
            throw new FacultyIdFailException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty(Long id){
        logger.info("Delete faculty method was invoked");
        Faculty facultYToDelete = getFaculty(id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColor(String color){
        logger.info("Find faculty by color method was invoked");
        return facultyRepository.findByColor(color);
    }
    public List<Faculty>findByNameIgnoreCaseOrColorIgnoreCase(String nameOrColor){
        logger.info("Find faculty by name or color ignoring case method was invoked");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor );
    };
    public List<Faculty>findAll(){
        logger.info("Find all faculties method was invoked");
        return facultyRepository.findAll();
    }

    public List<Student> findStudentsByFaculty_Id(Long id){
        logger.info("Find all students of faculty by ID method was invoked");
        Faculty faculty = getFaculty(id);
        return studentRepository.findByFaculty_Id(id);
    }
    public String getLongestName(){
        return facultyRepository.findAll().stream()
                .map(f -> f.getName())
                .max(Comparator.comparing(n ->n.toCharArray().length))
                .get();
    }



}
