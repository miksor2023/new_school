package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

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

    public Faculty postFaculty(Faculty faculty){
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id){
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyIdFailException(id));
    }
    public Faculty updateFaculty(Faculty faculty){
        if(!facultyRepository.existsById(faculty.getId())) {
            throw new FacultyIdFailException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }
    public Faculty deleteFaculty(Long id){
        Faculty facultYToDelete = getFaculty(id);
        facultyRepository.deleteById(id);
        return facultYToDelete;
    }

    public List<Faculty> findByColor(String color){
        return facultyRepository.findByColor(color);
    }
    public List<Faculty>findByNameOrColorIgnoreCase(String name, String color){
        return facultyRepository.findByNameOrColorIgnoreCase(name, color);
    };
    public List<Faculty>findAll(){
        return facultyRepository.findAll();
    }

    public List<Student> findByFaculty_Id(Long id){
        Faculty faculty = getFaculty(id);
        return studentRepository.findByFaculty_Id(id);
    }



}
