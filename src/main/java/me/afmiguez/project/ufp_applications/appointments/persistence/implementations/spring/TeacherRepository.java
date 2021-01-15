package me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findByUsername(String username);
}
