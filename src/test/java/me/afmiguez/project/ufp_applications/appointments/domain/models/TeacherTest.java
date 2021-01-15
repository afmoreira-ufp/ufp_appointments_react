package me.afmiguez.project.ufp_applications.appointments.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    Teacher teacher;
    Course course;
    ExtraordinarySchedule schedule1;
    ExtraordinarySchedule schedule2;
    AbstractSchedule schedule3;
    @BeforeEach
    public void setup(){
        schedule1=ExtraordinarySchedule.builder().dayOfWeek(LocalDate.now().getDayOfWeek()).course(course).startPeriod(LocalDate.now()).endPeriod(LocalDate.now().plusWeeks(1)).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build();
        schedule2=ExtraordinarySchedule.builder().dayOfWeek(LocalDate.now().getDayOfWeek()).course(course).startPeriod(LocalDate.now().plusWeeks(1)).endPeriod(LocalDate.now().plusWeeks(2)).startTime(LocalTime.of(16,0)).endTime(LocalTime.of(18,0)).build();
        schedule3=RegularSchedule.builder().dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek()).startTime(LocalTime.of(10,0)).endTime(LocalTime.of(12,0)).build();

        course=Course.builder().name("course1").build();
        teacher=Teacher.builder()
                .username("teacher1")
                .build();

        teacher.addSchedule(schedule1);
        teacher.addSchedule(schedule2);
        teacher.addSchedule(schedule3);

        Appointment appointment1= Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)))
                .teacher(teacher)
                .build();

        Appointment appointment2= Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,30)))
                .teacher(teacher)
                .build();


        teacher.addAppointment(appointment1);
        teacher.addAppointment(appointment2);
    }

    @Test
    void addAppointment(){
        setup();
        assertFalse(teacher.canAttendAppointment(Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)))
                .teacher(teacher)
                .build())
        );
        assertFalse(teacher.canAttendAppointment(Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now().plusDays(1),LocalTime.of(8,0)))
                .teacher(teacher)
                .build())
        );
        assertTrue(teacher.canAttendAppointment(Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(9,0)))
                .teacher(teacher)
                .build())
        );

    }

    @Test
    void getAllExtraordinarySchedules() {

        assertEquals(2,teacher.getAllExtraordinarySchedules().size());

        teacher=Teacher.builder().build();

        assertEquals(0,teacher.getAllExtraordinarySchedules().size());

    }

    @Test
    void countStudentAppointmentsByExtraordinaryPeriod() {
        assertEquals(2,teacher.countStudentAppointmentsByExtraordinaryPeriod(schedule1,Student.builder().username("student1").build()));
        assertEquals(0,teacher.countStudentAppointmentsByExtraordinaryPeriod(schedule1,Student.builder().username("student2").build()));

        teacher.addSchedule(ExtraordinarySchedule.builder().dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek()).course(course).startPeriod(LocalDate.now()).endPeriod(LocalDate.now().plusWeeks(1)).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build());

        Appointment appointment=Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now().plusDays(1),LocalTime.of(8,0)))
                .teacher(teacher)
                .build();

        teacher.addAppointment(appointment);

        assertEquals(3,teacher.countStudentAppointmentsByExtraordinaryPeriod(appointment));
    }

    @Test
    void getAppointmentsForExtraordinaryPeriod() {
        assertEquals(2,teacher.getAppointmentsForExtraordinaryPeriod(schedule1).size());
        assertEquals(0,teacher.getAppointmentsForExtraordinaryPeriod(schedule2).size());
    }

    @Test
    void isExtraordinary() {
        assertTrue(teacher.isExtraordinaryAppointment(Appointment.builder().startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0))).build()));
        assertFalse(teacher.isExtraordinaryAppointment(Appointment.builder().startHour(LocalDateTime.of(LocalDate.now().plusWeeks(2).plusDays(1),LocalTime.of(10,0))).build()));
    }

    @Test
    void extraordinarySchedulesForAppointment(){
        assertEquals(1,teacher.extraordinarySchedulesForAppointment(Appointment.builder()
                .student(Student.builder().username("student1").build())
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)))
                .teacher(teacher)
                .build()).size());
    }
}