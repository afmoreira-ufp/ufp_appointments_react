package me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    Optional<Authority> findByRole(String role);
}
