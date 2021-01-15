package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.SearchTeachers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchTeachersImpl implements SearchTeachers {

    private final TeacherDAO teacherDAO;

    public SearchTeachersImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherDAO.findAll();
    }
}
