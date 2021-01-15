package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Authority;
import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.AuthorityRepository;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AuthorityDAO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorityDAOImpl implements AuthorityDAO {
    private final AuthorityRepository authorityRepository;

    public AuthorityDAOImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public long count() {
        return authorityRepository.count();
    }

    @Override
    public Optional<Authority> findByRole(String role) {
        return authorityRepository.findByRole(role);
    }
}
