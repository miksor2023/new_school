package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.FacultyIdFailException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.TestConstants.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepositoryMock;
    @InjectMocks
    private FacultyService out;
    @Test
    public void postFacultyTest(){
        when(facultyRepositoryMock.save(TEST_FACULTY_WITH_WRONG_ID)).thenReturn(TEST_FACULTY_1);
        Faculty expected = TEST_FACULTY_1;
        Faculty actual = out.postFaculty(TEST_FACULTY_WITH_WRONG_ID);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void getFacultyPositiveTest(){
        when(facultyRepositoryMock.existsById(TEST_ID_1)).thenReturn(true);
        when(facultyRepositoryMock.findById(TEST_ID_1)).thenReturn(Optional.of(TEST_FACULTY_1));
        Faculty expected = TEST_FACULTY_1;
        Faculty actual = out.getFaculty(TEST_ID_1);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void getFacultMustThrowException(){
        when(facultyRepositoryMock.existsById(TEST_ID_1)).thenReturn(false);
        assertThrows(FacultyIdFailException.class, () -> out.getFaculty(TEST_ID_1));
    }
    @Test
    public void updateFacultyPositiveTest(){
        when(facultyRepositoryMock.existsById(TEST_FACULTY_1.getId())).thenReturn(true);
        when(facultyRepositoryMock.save(TEST_FACULTY_1)).thenReturn(TEST_FACULTY_1);
        Faculty expected = TEST_FACULTY_1;
        Faculty actual = out.updateFaculty(TEST_FACULTY_1);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void updateFacultyMustThrowException(){
        when(facultyRepositoryMock.existsById(TEST_FACULTY_1.getId())).thenReturn(false);
        assertThrows(FacultyIdFailException.class, () -> out.updateFaculty(TEST_FACULTY_1));
    }
    @Test
    public void deleteFacultyPositiveTest(){
        when(facultyRepositoryMock.existsById(TEST_ID_1)).thenReturn(true);
        when(facultyRepositoryMock.findById(TEST_ID_1)).thenReturn(Optional.of(TEST_FACULTY_1));
        Faculty expected = TEST_FACULTY_1;
        Faculty actual = out.deleteFaculty(TEST_ID_1);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void deleteFacultyMustThrowException(){
        when(facultyRepositoryMock.existsById(TEST_ID_1)).thenReturn(false);
        assertThrows(FacultyIdFailException.class, () -> out.deleteFaculty(TEST_ID_1));
    }
    @Test
    public void getFacultiesByColorTest(){
        when(facultyRepositoryMock.findAll()).thenReturn(TEST_FACULTIES_1);
        List<Faculty> expected = TEST_FACULTIES_2;
        List<Faculty> actual = out.getFacultiesByColor(TEST_COLOR);
        assertThat(expected).containsAll(actual);
    }
}
