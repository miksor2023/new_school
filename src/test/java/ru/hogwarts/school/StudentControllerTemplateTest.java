package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    static Long testId = 1L;
    static String testStudentName = "ann";
    static String newStudentTestName = "rick";
    static String testFacultyName = "testFaculty";
    static String testColor = "testColor";
    static Integer testAge = 21;
    static Integer newTestAge = 22;
    static int lowerAge = 20;
    static int upperAge = 23;


    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(testId);
        faculty.setName(testFacultyName);
        faculty.setColor(testColor);
        return facultyRepository.save(faculty);
    }

    private Student createStudentToPost() {
        Faculty faculty = createTestFaculty();
        Student student = new Student();
        student.setName(testStudentName);
        student.setAge(testAge);
        student.setFaculty(faculty);
        return student;
    }

    private Student createStudentToUpdate() {
        Student student = new Student();
        student.setName(newStudentTestName);
        student.setAge(newTestAge);
        return student;
    }

    @AfterEach
    public void deleteAllTables() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }


    @Test
    public void postStudentTest() throws Exception {
        Student returnedStudent = restTemplate.postForObject("http://localhost:" + port + "/student", createStudentToPost(), Student.class);
        Assertions.assertThat(returnedStudent.getId()).isNotNull();
        Assertions.assertThat(returnedStudent.getName()).isEqualTo(testStudentName);
        Assertions.assertThat(returnedStudent.getAge()).isEqualTo(testAge);
    }

    @Test
    public void getStudentByIdTest() throws Exception {
        Student postedStudent = studentRepository.save(createStudentToPost());
        Student returnedStudent = restTemplate.getForObject("http://localhost:" + port + "/student/" + postedStudent.getId(), Student.class);
        Assertions.assertThat(returnedStudent.getName()).isEqualTo(testStudentName);
        Assertions.assertThat(returnedStudent.getAge()).isEqualTo(testAge);
    }

    @Test
    public void getStudentsByAgeTest() throws Exception {
        studentRepository.save(createStudentToPost());
        ResponseEntity<Student[]> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/student?age={testAge}", Student[].class, testAge);
        Student[] students = entity.getBody();
        List<Integer> ages = Arrays.stream(students).map(student -> student.getAge()).collect(Collectors.toList());
        Assertions.assertThat(ages).containsOnly(testAge);
    }

    @Test
    public void getStudentsByAgeBetweenTest() throws Exception {
        studentRepository.save(createStudentToPost());
        ResponseEntity<Student[]> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/student?lowerAge={lowerAge}&upperAge={upperAge}", Student[].class, lowerAge, upperAge);
        Student[] students = entity.getBody();
        List<Integer> ages = Arrays.stream(students).map(student -> student.getAge()).collect(Collectors.toList());
        Assertions.assertThat(ages).contains(testAge);
    }

    @Test
    public void getFacultyOfStudentTest() throws Exception {
        Student postedStudent = studentRepository.save(createStudentToPost());
        Faculty expextedFaculty = postedStudent.getFaculty();
        Faculty actualFaculty = restTemplate
                .getForObject("http://localhost:" + port + "/student/{id}/faculty", Faculty.class, postedStudent.getId());
        Assertions.assertThat(actualFaculty).isEqualTo(expextedFaculty);

    }

    @Test
    public void updateStudentTest() throws Exception {
        Student postedStudent = studentRepository.save(createStudentToPost());
        Student studentToUpdate = createStudentToUpdate();
        studentToUpdate.setFaculty(postedStudent.getFaculty());
        studentToUpdate.setId(postedStudent.getId());
        HttpEntity<Student> entity = new HttpEntity<>(studentToUpdate);
        ResponseEntity<Student> responce = restTemplate
                .exchange("http://localhost:" + port + "/student", HttpMethod.PUT, entity, Student.class);
        Assertions.assertThat(responce.getBody()).isEqualTo(studentToUpdate);
    }

    @Test
    public void deleteStudentTest() throws Exception {
        Student postedStudent = studentRepository.save(createStudentToPost());
        ResponseEntity<String> responce = restTemplate
                .exchange("http://localhost:" + port + "/student/" + postedStudent.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(responce.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responce.getBody()).isEqualTo("Студент с ID %d удалён".formatted(postedStudent.getId()));
    }


}
