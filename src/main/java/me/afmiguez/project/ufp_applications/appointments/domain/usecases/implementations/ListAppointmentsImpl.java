package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.ListAppointments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListAppointmentsImpl implements ListAppointments {

    private final TeacherDAO teacherDAO;

    public ListAppointmentsImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public List<Appointment> listAppointments(Long teacherId) {
        Optional<Teacher> optionalTeacher=teacherDAO.findById(teacherId);
        if(optionalTeacher.isPresent()){
            return optionalTeacher.get().getAppointments();
        }
        throw new ResponsesException(HttpStatus.BAD_REQUEST,"Could not process use case");
    }
}
