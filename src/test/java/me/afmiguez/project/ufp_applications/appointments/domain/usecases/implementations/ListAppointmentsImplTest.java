package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.ListAppointments;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ListAppointmentsImpl.class)
class ListAppointmentsImplTest {

    @Autowired
    private ListAppointments listAppointments;

    @MockBean
    private TeacherDAO teacherDAO;

    @Test
    void listAppointments() {

        Teacher teacher= Teacher.builder()
                .id(1L)
                .appointments(Arrays.asList(
                        Appointment.builder()
                                .startHour(LocalDateTime.now())
                                .build(),
                        Appointment.builder()
                                .startHour(LocalDateTime.now().plusHours(1))
                                .build()
                ))
                .build();

        when(teacherDAO.findById(1L)).thenReturn(Optional.of(teacher));

        List<Appointment> optionalAppointments=listAppointments.listAppointments(1L);
        assertNotNull(optionalAppointments);
        assertEquals(2,optionalAppointments.size());

        assertThrows(ResponsesException.class,()->listAppointments.listAppointments(2L));

    }
}