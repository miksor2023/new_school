package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.IdFailException;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student postStudent(Student student){
        student.setId(null);
        return studentRepository.save(student);
    }
    public Student getStudent(Long id){
        return studentRepository.findById(id).orElseThrow(() -> new StudentIdFailException(id));
    }
    public Student updateStudent(Student student){
        if(!studentRepository.existsById(student.getId())) {
            throw new StudentIdFailException(student.getId());
        }
        return studentRepository.save(student);
    }
    public Student deleteStudent(Long id){
        Student studentToDelete = studentRepository.findById(id).orElseThrow(() -> new StudentIdFailException(id));
        studentRepository.deleteById(id);
        return studentToDelete;
    }
    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }
    public void validateId(Long id) throws StudentIdFailException {
        if(!studentRepository.existsById(id)) {
            throw new StudentIdFailException(id);
        }
    }
}
