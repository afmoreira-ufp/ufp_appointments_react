package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.UserRepository;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.UserDAO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<AbstractUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AbstractUser save(AbstractUser abstractUser) {
        return userRepository.save(abstractUser);
    }

    @Override
    public boolean contains(String username) {
        return userRepository.existsByUsername(username);
    }
}
