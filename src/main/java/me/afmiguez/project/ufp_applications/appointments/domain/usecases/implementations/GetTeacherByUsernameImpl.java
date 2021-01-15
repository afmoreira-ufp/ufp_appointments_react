package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetTeacherByUsername;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTeacherByUsernameImpl implements GetTeacherByUsername {

    private final TeacherDAO teacherDAO;

    @Override
    public Teacher getTeacherByUsername(String username) {
        return teacherDAO.findByUsername(username).orElse(null);
    }
}
