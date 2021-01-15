package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CancelAppointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CancelAppointmentImpl.class)
class CancelAppointmentImplTest {

    @MockBean
    private StudentDAO studentDAO;

    @MockBean
    private TeacherDAO teacherDAO;

    @MockBean
    private AppointmentDAO appointmentDAO;

    @MockBean
    private CalendarService calendarService;

    @Autowired
    private CancelAppointment cancelAppointment;



    @Test
    void cancelAppointment() {
        assertNotNull(studentDAO);
        assertNotNull(teacherDAO);
        assertNotNull(appointmentDAO);
        assertNotNull(cancelAppointment);

        Appointment appointment= Appointment.builder()
                .startHour(LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(8,0)
                ))
                .build();


        Teacher teacher= Teacher.builder()
                .id(1L)
                .schedules(Collections.singletonList(RegularSchedule.builder()
                        .dayOfWeek(LocalDate.now().getDayOfWeek())
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .build()))
                .build();

        Student student= Student.builder()
                .id(1L)
                .username("student")
                .build();

        teacher.addAppointment(appointment);
        student.addAppointment(appointment);

        when(studentDAO.findById(1L)).thenReturn(Optional.of(student));
        when(teacherDAO.findById(1L)).thenReturn(Optional.of(teacher));
        when(appointmentDAO.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentDAO.delete(1L)).thenReturn(appointment);

        Appointment optionalAppointment=cancelAppointment.cancelAppointment(1L,"student");
        assertNotNull(optionalAppointment);

        //when(appointmentDAO.findById(1L)).thenThrow(new ResponsesException(HttpStatus.BAD_REQUEST,"Could not process use case"));
        when(appointmentDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponsesException.class,()->cancelAppointment.cancelAppointment(1L,""));

        when(studentDAO.findByUsername("not user")).thenReturn(Optional.empty());

        assertThrows(ResponsesException.class,()->cancelAppointment.cancelAppointment(1L,"not user"));

    }
}