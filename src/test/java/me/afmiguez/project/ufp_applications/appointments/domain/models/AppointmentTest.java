package me.afmiguez.project.ufp_applications.appointments.domain.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    void convertDST(){
        LocalDate date=LocalDate.now();
        Appointment appointment= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date,LocalTime.of(14,0)))
                .build();

        assertTrue(appointment.getExpectedEndTime().isAfter(appointment.convertDST().getStartHour()));

    }

    @Test
    void overlaps() {

        LocalDate date=LocalDate.now();

        Appointment appointment= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date,LocalTime.of(14,0)))
                .build();

        Appointment appointment1= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(6,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(11,0)))
                .build();

        assertFalse(appointment.overlaps(appointment1));

        Appointment appointment2= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(12,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(16,0)))
                .build();

        assertFalse(appointment.overlaps(appointment2));

        Appointment appointment3= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(14,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(15,0)))
                .build();

        assertFalse(appointment.overlaps(appointment3));

        Appointment appointment4= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(7,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(8,0)))
                .build();

        assertFalse(appointment.overlaps(appointment4));

        Appointment appointment5= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(9,0)))
                .build();

        assertTrue(appointment.overlaps(appointment5));

        Appointment appointment6= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(13,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(14,0)))
                .build();

        assertTrue(appointment.overlaps(appointment6));

        Appointment appointment7= Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(9,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(10,0)))
                .build();

        assertTrue(appointment.overlaps(appointment7));
    }
}