package me.afmiguez.project.ufp_applications.appointments.domain.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ExtraordinaryScheduleTest {

    @Test
    void contains() {

        LocalDate date=LocalDate.now();

        Schedule schedule=ExtraordinarySchedule.builder()
                .dayOfWeek(date.getDayOfWeek())
                .startPeriod(LocalDate.now())
                .endPeriod(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(8,0))
                .endTime(LocalTime.of(14,0))
                .build();

        Appointment appointment1=Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(6,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(11,0)))
                .build();

        assertFalse(schedule.contains(appointment1));

        Appointment appointment2=Appointment.builder()
                .startHour(LocalDateTime.of(date.plusWeeks(1).plusDays(1), LocalTime.of(6,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(11,0)))
                .build();

        assertFalse(schedule.contains(appointment2));

        Appointment appointment3=Appointment.builder()
                .startHour(LocalDateTime.of(date, LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date, LocalTime.of(8,30)))
                .build();

        assertTrue(schedule.contains(appointment3));

    }
}