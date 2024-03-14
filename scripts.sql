SELECT * FROM student WHERE age BETWEEN 5 AND 25
SELECT name FROM student
SELECT * FROM student WHERE name LIKE '%o%'
SELECT * FROM public.student WHERE age < id
SELECT * FROM public.student ORDER BY age
SELECT * FROM student, faculty where student.faculty_id = faculty.id and faculty.id = 1
SELECT COUNT(*) AS sum FROM student
SELECT AVG(age) AS average FROM student
SELECT * FROM student ORDER BY id DESC LIMIT 5