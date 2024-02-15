package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private FacultyRepository facultyRepository;

    public FacultyService() {
        this.facultyRepository = facultyRepository;
    }

    public Faculty postFaculty(Faculty faculty){
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id){
        validateId(id);
        return facultyRepository.findById(id).get();
    }
    public Faculty updateFaculty(Faculty faculty){
        validateId(faculty.getId());
        return facultyRepository.save(faculty);
    }
    public Faculty deleteFaculty(Long id){
        validateId(id);
        Faculty facultYToDelete = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        return facultYToDelete;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        List<Faculty> faculties = facultyRepository.findAll();
        List<Faculty> facultiesByColor = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
        return facultiesByColor;
    }
    public void validateId(Long id) throws FacultyIdFailException {
        if(!facultyRepository.existsById(id)){
            throw new FacultyIdFailException(id);
        }
    }

}
