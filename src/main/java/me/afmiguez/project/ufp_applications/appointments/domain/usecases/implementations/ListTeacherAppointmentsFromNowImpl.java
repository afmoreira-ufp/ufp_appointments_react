package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.ListTeacherAppointmentsFromNow;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListTeacherAppointmentsFromNowImpl implements ListTeacherAppointmentsFromNow {

    private final TeacherDAO teacherDAO;

    @Override
    public List<Appointment> listAppointmentsFromNow(String teacherUsername) {
        Optional<Teacher> optionalTeacher=teacherDAO.findByUsername(teacherUsername);
        if(optionalTeacher.isPresent()){
            Teacher teacher=optionalTeacher.get();
            return teacher.getAppointments().stream()
                    .filter(appointment -> (appointment.getStartHour().isAfter(LocalDateTime.now()) || appointment.getStartHour().equals(LocalDateTime.now())  ))
                    .sorted(Comparator.comparing(Appointment::getStartHour))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
