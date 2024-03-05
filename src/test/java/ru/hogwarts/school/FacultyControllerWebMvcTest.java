package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;


import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private FacultyService facultyService;
    @InjectMocks
    private FacultyController facultyController;

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


    private Faculty createFacultyToPost() {
        Faculty faculty = new Faculty();
        faculty.setId(testId);
        faculty.setName(testName);
        faculty.setColor(testColor);
        return faculty;
    }

    private Faculty createFacultyToUpdate() {
        Faculty faculty = new Faculty();
        faculty.setId(testId);
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
    public void postFacultitTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testName))
                .andExpect(jsonPath("$.color").value(testColor));
    }

    @Test
    public void getFacultyByIdTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(testName))
                .andExpect(jsonPath("$.color").value(testColor));
    }

    @Test
    public void getFacultysByColorTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        List<Faculty> faculties = List.of(faculty);
        when(facultyRepository.findByColor(testColor)).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color={testColor}", testColor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testColor)));
    }

    @Test
    public void getFacultiesByNameOrColorIgnoreCaseTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        List<Faculty> faculties = List.of(faculty);
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(testName, testName)).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?nameOrColor={nameOrColor}", testName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testName)));
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(testColor, testColor)).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?nameOrColor={nameOrColor}", testColor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testColor)));
    }

    @Test
    public void getStudentsOfFacultyTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        Student student = createTestStudent();
        student.setFaculty(faculty);
        List<Student> students = List.of(student);
        when(facultyRepository.findById(testId)).thenReturn(Optional.of(faculty));
        when(studentRepository.findByFaculty_Id(testId)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}/students", testId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testStudentName)))
                .andExpect(content().string(containsString(testId.toString())))
                .andExpect(content().string(containsString(testAge.toString())));
    }

    @Test
    public void updateFacultyTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        Faculty newFaculty = createFacultyToUpdate();
        when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(newFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(newTestName))
                .andExpect(jsonPath("$.color").value(newTestColor));
    }
    @Test
    public void deleteFacultyTest() throws Exception {
        Faculty faculty = createFacultyToPost();
        when(facultyRepository.findById(testId)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculty/{id}", faculty.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Факультет с ID %d удалён".formatted(faculty.getId())));
    }
}






