package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.IncorrectIdException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private Long idCounter = 0L;
    public Faculty postFaculty(Faculty faculty){
        faculty.setId(++idCounter);
        faculties.put(idCounter, faculty);
        return faculty;
    }
    public Faculty getFaculty(Long id){
        validateId(id);
        return faculties.get(id);
    }
    public Faculty updateFaculty(Faculty faculty){
        validateId(faculty.getId());
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }
    public Faculty deleteFaculty(Long id){
        validateId(id);
        return faculties.remove(id);
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        List<Faculty> facultiesByColor = faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
        return facultiesByColor;
    }
    public void validateId(Long id) throws IncorrectIdException {
        if(!faculties.containsKey(id)){
            throw new IncorrectIdException();
        }
    }

}
