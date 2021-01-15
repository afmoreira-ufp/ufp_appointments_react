package me.afmiguez.project.ufp_applications.appointments.daos;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherDAO {
    List<Teacher> findAll();

    Optional<Teacher> findById(Long teacherId);

    Teacher save(Teacher teacher);

    Optional<Teacher> findByUsername(String username);
}
