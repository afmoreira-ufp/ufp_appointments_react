package me.afmiguez.project.ufp_applications.appointments.persistence.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Authority;

import java.util.Optional;

public interface AuthorityDAO {

    Authority save(Authority admin);

    long count();

    Optional<Authority> findByRole(String role);
}
