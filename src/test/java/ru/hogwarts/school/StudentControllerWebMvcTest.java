package ru.hogwarts.school;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;
    static Long testId = 1L;
    static String testStudentName = "ann";
    static String newStudentTestName = "rick";
    static String testFacultyName = "testFaculty";
    static String testColor = "testColor";
    static Integer testAge = 21;
    static Integer newTestAge = 22;
    static int lowerAge = 20;
    static int upperAge = 23;


    public Student createTestStudentWithFaculty() {
        Student student = new Student();
        student.setId(testId);
        student.setName(testStudentName);
        student.setAge(testAge);
        student.setFaculty(createTestFaculty());
        return student;
    }

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(testId);
        faculty.setName(testFacultyName);
        faculty.setColor(testColor);
        return faculty;
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

    @Test
    public void postStudentTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        Faculty faculty = createTestFaculty();
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testStudentName))
                .andExpect(jsonPath("$.age").value(testAge));
    }
    @Test
    public void getStudentByIdTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testStudentName))
                .andExpect(jsonPath("$.age").value(testAge));
    }
    @Test
    public void getStudentsByAgeTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        List<Student> students = List.of(student);
        when(studentRepository.findByAge(testAge)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age={testAge}", testAge)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testAge.toString())));
    }
    @Test
    public void getStudentsByAgeBetweenTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        List<Student> students = List.of(student);
        when(studentRepository.findByAgeBetween(lowerAge, upperAge)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?lowerAge={lowerAge}&upperAge={upperAge}", lowerAge, upperAge)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testAge.toString())));
    }
    @Test
    public void getFacultyOfStudentTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        Faculty faculty = createTestFaculty();
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/student/{id}/faculty", student.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testFacultyName))
                .andExpect(jsonPath("$.color").value(testColor));
    }
    @Test
    public void updateStudentTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        Faculty faculty = createTestFaculty();
        when(studentRepository.existsById(any(Long.class))).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testStudentName))
                .andExpect(jsonPath("$.age").value(testAge));
    }
    @Test
    public void deleteStudentTest() throws Exception {
        Student student = createTestStudentWithFaculty();
        when(studentRepository.findById(testId)).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Студент с ID %d удалён".formatted(student.getId())));
    }
}




