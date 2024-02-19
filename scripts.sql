SELECT * FROM student WHERE age > 5 AND age < 25
SELECT name FROM student
SELECT * FROM student WHERE name LIKE ('%o%')
SELECT * FROM public.student WHERE age < id
SELECT * FROM public.student ORDER BY age
SELECT * FROM student, faculty where student.faculty_id = faculty.id and faculty.id = 1;