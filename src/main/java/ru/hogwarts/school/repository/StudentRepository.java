package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int lowerAge, int upperAge);
    List<Student> findByFaculty_Id(long id);
    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    int getStudentsQty();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    int getStudensAverageAge();
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFive();
}
