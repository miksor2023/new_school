package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.StudentIdFailException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.TestConstants.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepositoryMock;
    @InjectMocks
    private StudentService out;

    @Test
    public void postStudentTest() {
        when(studentRepositoryMock.save(TEST_STUDENT_WITH_WRONG_ID)).thenReturn(TEST_STUDENT_1);
        Student expected = TEST_STUDENT_1;
        Student actual = out.postStudent(TEST_STUDENT_WITH_WRONG_ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getStudentPositiveTest() {
        when(studentRepositoryMock.existsById(TEST_ID_1)).thenReturn(true);
        when(studentRepositoryMock.findById(TEST_ID_1)).thenReturn(Optional.of(TEST_STUDENT_1));
        Student expected = TEST_STUDENT_1;
        Student actual = out.getStudent(TEST_ID_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getFacultMustThrowException() {
        when(studentRepositoryMock.existsById(TEST_ID_1)).thenReturn(false);
        assertThrows(StudentIdFailException.class, () -> out.getStudent(TEST_ID_1));
    }

    @Test
    public void updateStudentPositiveTest() {
        when(studentRepositoryMock.existsById(TEST_STUDENT_1.getId())).thenReturn(true);
        when(studentRepositoryMock.save(TEST_STUDENT_1)).thenReturn(TEST_STUDENT_1);
        Student expected = TEST_STUDENT_1;
        Student actual = out.updateStudent(TEST_STUDENT_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStudentMustThrowException() {
        when(studentRepositoryMock.existsById(TEST_STUDENT_1.getId())).thenReturn(false);
        assertThrows(StudentIdFailException.class, () -> out.updateStudent(TEST_STUDENT_1));
    }

    @Test
    public void deleteStudentPositiveTest() {
        when(studentRepositoryMock.existsById(TEST_ID_1)).thenReturn(true);
        when(studentRepositoryMock.findById(TEST_ID_1)).thenReturn(Optional.of(TEST_STUDENT_1));
        Student expected = TEST_STUDENT_1;
        Student actual = out.deleteStudent(TEST_ID_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deleteStudentMustThrowException() {
        when(studentRepositoryMock.existsById(TEST_ID_1)).thenReturn(false);
        assertThrows(StudentIdFailException.class, () -> out.deleteStudent(TEST_ID_1));
    }

//    @Test
//    public void getFacultiesByColorTest() {
//        when(studentRepositoryMock.findAll()).thenReturn(TEST_STUDENTS_1);
//        List<Student> expected = TEST_STUDENTS_2;
//        List<Student> actual = out.getStudentsByAge(TEST_AGE);
//        assertThat(expected).containsAll(actual);
//    }
}