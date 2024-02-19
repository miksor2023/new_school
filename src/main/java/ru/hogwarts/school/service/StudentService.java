package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

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
    public List<Student> findByAgeBetween(int lowerAge, int upperAge) {
        return studentRepository.findByAgeBetween(lowerAge, upperAge);
    }
    public List<Student> findAll(){
        return studentRepository.findAll();
    }
    public String getFaculty(Long id){
        return studentRepository.findById(id).orElseThrow(() -> new StudentIdFailException(id)).getFaculty();
    }
}
