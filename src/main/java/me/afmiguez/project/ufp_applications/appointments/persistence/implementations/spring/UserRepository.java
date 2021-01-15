package me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring;

import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AbstractUser,Long> {
    Optional<AbstractUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
