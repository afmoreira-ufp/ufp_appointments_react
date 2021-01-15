package me.afmiguez.project.ufp_applications.appointments.daos;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;

import java.util.Optional;

public interface StudentDAO {

    Optional<Student> findById(Long studentId);

    Student save(Student student);

    Optional<Student> findByUsername(String username);
}
