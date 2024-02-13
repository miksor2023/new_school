package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.IncorrectIdException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private Long idCounter = 0L;
    public Student postStudent(Student student){
        student.setId(++idCounter);
        students.put(idCounter, student);
        return student;
    }
    public Student getStudent(Long id){
        validateId(id);
        return students.get(id);
    }
    public Student updateStudent(Student student){
        validateId(student.getId());
        students.put(student.getId(), student);
        return student;
    }
    public Student deleteStudent(Long id){
        validateId(id);
        return students.remove(id);
    }
    public Collection<Student> getStudentsByAge(int age) {
        List<Student> studentsByAge = students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
        return studentsByAge;
    }
    public void validateId(Long id) throws IncorrectIdException {
        if(!students.containsKey(id)){
            throw new IncorrectIdException();
        }
    }


}
