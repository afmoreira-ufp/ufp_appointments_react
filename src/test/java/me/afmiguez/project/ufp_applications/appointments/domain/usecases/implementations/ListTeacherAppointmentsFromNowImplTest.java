package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.ListTeacherAppointmentsFromNow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ListTeacherAppointmentsFromNowImpl.class)
class ListTeacherAppointmentsFromNowImplTest {

    @Autowired
    private ListTeacherAppointmentsFromNow listTeacherAppointmentsFromNow;

    @MockBean
    private TeacherDAO teacherDAO;

    @Test
    void listAppointmentsFromNow() {
        LocalDate now=LocalDate.now();
        List<Appointment> appointmentList= Arrays.asList(
                Appointment.builder().startHour(LocalDateTime.of(now.minusDays(1), LocalTime.of(8,0))).build(),
                Appointment.builder().startHour(LocalDateTime.of(now.plusDays(1), LocalTime.of(8,0))).build()
        );

        Teacher teacher=Teacher.builder()
                .username("teacher1")
                .appointments(appointmentList)
                .build();

        when(teacherDAO.findByUsername("teacher1")).thenReturn(Optional.of(teacher));

        assertEquals(1,listTeacherAppointmentsFromNow.listAppointmentsFromNow("teacher1").size());

        assertEquals(0,listTeacherAppointmentsFromNow.listAppointmentsFromNow("nonExistentTeacher").size());


    }
}