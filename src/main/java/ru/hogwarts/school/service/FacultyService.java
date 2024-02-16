package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty postFaculty(Faculty faculty){
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id){
        return facultyRepository.findById(id).orElseThrow(() -> FacultyIdFailException(id));
    }
    public Faculty updateFaculty(Faculty faculty){
        if(!FacultyRepository.existsById(faculty.getId())) {
            throw new StudentIdFailException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }
    public Faculty deleteFaculty(Long id){
        Faculty facultYToDelete = facultyRepository.findById(id).orElseThrow(() -> FacultyIdFailException(id));
        facultyRepository.deleteById(id);
        return facultYToDelete;
    }
    public List<Faculty> findByColor(String color){
        return facultyRepository.findByColor(color);
    }
    public void validateId(Long id) throws FacultyIdFailException {
        if(!facultyRepository.existsById(id)){
            throw new FacultyIdFailException(id);
        }
    }

}
