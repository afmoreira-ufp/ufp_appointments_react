package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.UserDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.CustomUserDetailsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static me.afmiguez.project.ufp_applications.appointments.Utils.getAuthorities;

@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public CustomUserDetailsUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AbstractUser abstractUser = userDAO.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User name: "+username+" not found"));

        return  CustomUserDetailsUser.builder()
                .id(abstractUser.getId())
                .credentialsNonExpired(abstractUser.isCredentialsNonExpired())
                .accountNonLocked(abstractUser.isAccountNonLocked())
                .accountNonExpired(abstractUser.isAccountNonExpired())
                .enabled(abstractUser.isEnabled())
                .username(abstractUser.getUsername())
                .authorities(getAuthorities(abstractUser.getAuthorities()))
                .build();
    }


    public AbstractUser save(AbstractUser abstractUser) {
        return userDAO.save(abstractUser);
    }
}
