package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.StudentRepository;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StudentDAOImpl implements StudentDAO {

    private final StudentRepository studentRepository;

    public StudentDAOImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
}
