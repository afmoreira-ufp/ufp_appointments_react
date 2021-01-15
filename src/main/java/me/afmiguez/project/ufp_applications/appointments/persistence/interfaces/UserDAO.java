package me.afmiguez.project.ufp_applications.appointments.persistence.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;

import java.util.Optional;

public interface UserDAO {
    Optional<AbstractUser> findByUsername(String username);

    AbstractUser save(AbstractUser abstractUser);

    boolean contains(String username);
}
