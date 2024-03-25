-- liquibase formatted sql

-- changeset michael:1
CREATE INDEX student_name_idx ON student (name);

-- changeset michael:2
CREATE INDEX faculty_nc_idx ON faculty (name, color);
