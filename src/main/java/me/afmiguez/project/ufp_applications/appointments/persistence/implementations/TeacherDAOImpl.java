package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.TeacherRepository;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private final TeacherRepository repository;

    public TeacherDAOImpl(TeacherRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Teacher> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Teacher> findById(Long teacherId) {
        return repository.findById(teacherId);
    }

    @Override
    public Teacher save(Teacher teacher) {
        return repository.save(teacher);
    }

    @Override
    public Optional<Teacher> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
