package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.IdFailException;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student postStudent(Student student){
        student.setId(null);
        return studentRepository.save(student);
    }
    public Student getStudent(Long id){
        validateId(id);
        return studentRepository.findById(id).get();
    }
    public Student updateStudent(Student student){
        validateId(student.getId());
        return studentRepository.save(student);
    }
    public Student deleteStudent(Long id){
        validateId(id);
        Student studentToDelete = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        return studentToDelete;
    }
    public List<Student> getStudentsByAge(int age) {
        List<Student> students = studentRepository.findAll();
        List<Student> studentsByAge = students.stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
        return studentsByAge;
    }
    public void validateId(Long id) throws StudentIdFailException {
        if(!studentRepository.existsById(id)) {
            throw new StudentIdFailException(id);
        }
    }


}
