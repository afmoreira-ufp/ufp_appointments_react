package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.CourseRepository;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDAOImpl implements CourseDAO {
    private final CourseRepository courseRepository;

    public CourseDAOImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findByName(String name) {
        return courseRepository.findByName(name);
    }
}
