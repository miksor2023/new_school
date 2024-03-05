package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student postStudent(Student student) {
        student.setId(null);
        Long facultyID = student.getFaculty().getId();
        if (facultyID != null) {
            Faculty faculty = facultyRepository.findById(facultyID).
                    orElseThrow(() -> new FacultyIdFailException(facultyID));
            student.setFaculty(faculty);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new StudentIdFailException(student.getId());
        }
        Long facultyID = student.getFaculty().getId();
        if (facultyID != null) {
            Faculty faculty = facultyRepository.findById(facultyID).
                    orElseThrow(() -> new FacultyIdFailException(facultyID));
            student.setFaculty(faculty);
        }
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentIdFailException(id));
    }

    public void deleteStudent(Long id) {
        Student studentToDelete = getStudent(id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int lowerAge, int upperAge) {
        return studentRepository.findByAgeBetween(lowerAge, upperAge);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Faculty getFaculty(Long id) {
        return getStudent(id).getFaculty();
    }


}
