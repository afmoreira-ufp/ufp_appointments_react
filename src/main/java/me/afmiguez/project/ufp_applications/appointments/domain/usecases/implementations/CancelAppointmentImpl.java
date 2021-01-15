package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CancelAppointment;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancelAppointmentImpl implements CancelAppointment {

    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final AppointmentDAO appointmentDAO;
    private final CalendarService calendarService;

    @Override
    public Appointment cancelAppointment(Long id, String actualUser) {
        Optional<Appointment> optionalAppointment=appointmentDAO.findById(id);
        if(optionalAppointment.isPresent()){
            Appointment appointment=optionalAppointment.get();
            Optional<Student> studentOptional = studentDAO.findById(appointment.getStudent().getId());

            if(studentOptional.isPresent() ){
                Student student=studentOptional.get();

                if(student.getUsername().equals(actualUser)){
                    Optional<Teacher> teacherOptional = teacherDAO.findById(appointment.getTeacher().getId());
                    if( teacherOptional.isPresent()){
                        Teacher teacher=teacherOptional.get();

                        calendarService.removeGCalEvent(appointment);
                        
                        teacher.removeAppointment(appointment);
                        student.removeAppointment(appointment);

                        teacherDAO.save(teacher);
                        studentDAO.save(student);

                        return appointment;
                    }
                }

            }

        }

        throw new ResponsesException(HttpStatus.BAD_REQUEST,"Could not cancel this appointment");
    }
}
