package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student postStudent(Student student) {
        logger.info("Post student method was invoked");
        student.setId(null);
        Faculty faculty = student.getFaculty();
        if (faculty != null && faculty.getId() != null) {
            Faculty facultyFromDB = facultyRepository.findById(faculty.getId()).
                    orElseThrow(() -> {
                        logger.error("There is not faculty with ID = " + faculty.getId());
                        return new FacultyIdFailException(faculty.getId());
                    });
            student.setFaculty(facultyFromDB);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        logger.info("Update student method was invoked");
        if (!studentRepository.existsById(student.getId())) {
            throw new StudentIdFailException(student.getId());
        }
        Long facultyID = student.getFaculty().getId();
        if (facultyID != null) {
            Faculty faculty = facultyRepository.findById(facultyID).
                    orElseThrow(() -> {
                        logger.error("There is not faculty with ID = " + facultyID);
                        return new FacultyIdFailException(facultyID);
                    });
            student.setFaculty(faculty);
        }
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Get student by ID method was invoked");
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with ID = " + id);
            return new StudentIdFailException(id);
        });
    }

    public void deleteStudent(Long id) {
        logger.info("Delete student by ID method was invoked");
        Student studentToDelete = getStudent(id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        logger.info("Find student by age method was invoked");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int lowerAge, int upperAge) {
        logger.info("Find student by age between method was invoked");
        return studentRepository.findByAgeBetween(lowerAge, upperAge);
    }

    public List<Student> findAll() {
        logger.info("Find all students method was invoked");
        return studentRepository.findAll();
    }

    public Faculty getFaculty(Long id) {
        logger.info("Get faculty of student by ID method was invoked");
        return getStudent(id).getFaculty();
    }


    public int getStudensQty() {
        logger.info("Get number of students method was invoked");
        return studentRepository.getStudentsQty();
    }

    public double getStudensAverageAge() {
        logger.info("Get average age of students method was invoked");
        return studentRepository.findAll().stream()
                .mapToInt((s -> s.getAge()))
                //.mapToDouble(s -> Double.valueOf(s))
                .average()
                .getAsDouble();
        //return studentRepository.getStudensAverageAge();
    }

    public List<Student> getLastFive() {
        logger.info("Get last five students method was invoked");
        return studentRepository.getLastFive();
    }

    public List<String> getNamesStartsWithA() {
        return studentRepository.findAll().stream()
                .map(s -> s.getName().toUpperCase())
                .filter(n -> n.startsWith("A"))
                .sorted()
                .toList();
     }
}
