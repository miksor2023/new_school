ALTER TABLE student ADD CONSTRAINT age_constraint CHECK (age >= 16)
ALTER TABLE student ADD CONSTRAINT name_unique_notnull UNIQUE NOT NULL (name)
ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color)
ALTER TABLE student ADD CONSTRAINT age_default DEFAULT 20 FOR age --не уверен, что это правильно


