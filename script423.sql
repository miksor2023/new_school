SELECT student.name, student.age, faculty.name
FROM student
JOIN faculty ON student.faculty_id = faculty.id

SELECT student.id, student.name
FROM student
JOIN avatar ON student_id = student.id