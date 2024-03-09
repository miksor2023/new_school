package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TestRestTemplate restTemplate;
    static Long testId = 1L;
    static String testName = "testFaculty";
    static String testColor = "testColor";
    static String newTestName = "newTestFaculty";
    static String newTestColor = "newTeatColor";
    static Integer testAge = 21;
    static String testStudentName = "sally";
    static Integer newTestAge = 22;
    static int lowerAge = 20;
    static int upperAge = 23;

    @AfterEach
    public void deleteAllTables() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private Faculty createFacultyToPost() {
        Faculty faculty = new Faculty();
        faculty.setId(testId);
        faculty.setName(testName);
        faculty.setColor(testColor);
        return faculty;
    }

    private Faculty createFacultyToUpdate() {
        Faculty faculty = new Faculty();
        faculty.setName(newTestName);
        faculty.setColor(newTestColor);
        return faculty;
    }

    private Student createTestStudent() {
        Student student = new Student();
        student.setId(testId);
        student.setName(testStudentName);
        student.setAge(testAge);
        return student;
    }


    @Test
    public void postFacultyTest() throws Exception {
        Faculty returnedFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", createFacultyToPost(), Faculty.class);
        Assertions.assertThat(returnedFaculty.getId()).isNotNull();
        Assertions.assertThat(returnedFaculty.getName()).isEqualTo(testName);
        Assertions.assertThat(returnedFaculty.getColor()).isEqualTo(testColor);
    }

    @Test
    public void getFacultyByIdTest() throws Exception {
        Faculty postedFaculty = facultyRepository.save(createFacultyToPost());
        Faculty returnedFaculty = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + postedFaculty.getId(), Faculty.class);
        Assertions.assertThat(returnedFaculty.getName()).isEqualTo(testName);
        Assertions.assertThat(returnedFaculty.getColor()).isEqualTo(testColor);
    }

    @Test
    public void getFacultysByColorTest() throws Exception {
        facultyRepository.save(createFacultyToPost());
        ResponseEntity<Faculty[]> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/faculty?color={testColor}", Faculty[].class, testColor);
        Faculty[] faculties = entity.getBody();
        List<String> colors = Arrays.stream(faculties).map(faculty -> faculty.getColor()).collect(Collectors.toList());
        Assertions.assertThat(colors).containsOnly(testColor);
    }

    @Test
    public void getFacultiesByNameOrColorIgnoreCaseTest() throws Exception {
        facultyRepository.save(createFacultyToPost());
        ResponseEntity<Faculty[]> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/faculty?nameOrColor={nameOrColor}", Faculty[].class, testName.toUpperCase());
        Faculty[] facultys = entity.getBody();
        List<String> names = Arrays.stream(facultys).map(faculty -> faculty.getName()).collect(Collectors.toList());
        Assertions.assertThat(names).containsOnly(testName);
        entity = restTemplate
                .getForEntity("http://localhost:" + port + "/faculty?nameOrColor={nameOrColor}", Faculty[].class, testColor.toUpperCase());
        facultys = entity.getBody();
        List<String> colors = Arrays.stream(facultys).map(faculty -> faculty.getColor()).collect(Collectors.toList());
        Assertions.assertThat(colors).containsOnly(testColor);
    }

    @Test
    public void getStudentsOfFacultyTest() throws Exception {
        Faculty postedFaculty = facultyRepository.save(createFacultyToPost());
        Student studentToPost = createTestStudent();
        studentToPost.setFaculty(postedFaculty);
        Student postedStudent = studentService.postStudent(studentToPost);
        ResponseEntity<Student[]> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/faculty/{id}/students", Student[].class, postedFaculty.getId());
        Student[] students = entity.getBody();
        Assertions.assertThat(students).contains(postedStudent);
    }

    @Test
    public void updateFacultyTest() throws Exception {
        Faculty postedFaculty = facultyRepository.save(createFacultyToPost());
        Faculty facultyToUpdate = createFacultyToUpdate();
        facultyToUpdate.setId(postedFaculty.getId());
        HttpEntity<Faculty> entity = new HttpEntity<>(facultyToUpdate);
        ResponseEntity<Faculty> responce = restTemplate
                .exchange("http://localhost:" + port + "/faculty", HttpMethod.PUT, entity, Faculty.class);
        Assertions.assertThat(responce.getBody()).isEqualTo(facultyToUpdate);
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        Faculty postedFaculty = facultyRepository.save(createFacultyToPost());
        ResponseEntity<String> responce = restTemplate
                .exchange("http://localhost:" + port + "/faculty/" + postedFaculty.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(responce.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responce.getBody()).isEqualTo("Факультет с ID %d удалён".formatted(postedFaculty.getId()));
    }
}