CREATE TABLE public.cars
(
id INTEGER PRIMARY KEY,
brand VARCHAR (255) NOT NULL,
model VARCHAR (255) NOT NULL,
price INT NOT NULL
);

CREATE TABLE public.people
(
id INT PRIMARY KEY,
age INT NOT NULL,
license BOOLEAN NOT NULL,
car_id INT,
FOREIGN KEY (car_id) REFERENCES public.cars (id)
);
