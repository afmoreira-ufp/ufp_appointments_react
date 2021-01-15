package me.afmiguez.project.ufp_applications.appointments.persistence.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    Course save(Course course);

    Optional<Course> findById(Long id);

    List<Course> findAll();

    Optional<Course> findByName(String name);
}
