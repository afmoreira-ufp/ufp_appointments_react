package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.MakeAppointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MakeAppointmentImpl.class)
class MakeAppointmentImplTest {

    @MockBean
    private StudentDAO studentDAO;

    @MockBean
    private TeacherDAO teacherDAO;
    @MockBean
    private AppointmentDAO appointmentDAO;
    @MockBean
    private CalendarService calendarService;
    @MockBean
    private CourseDAO courseDAO;

    @Autowired
    private MakeAppointment makeAppointment;

    @Test
    void makeAppointment() {
        assertNotNull(studentDAO);
        assertNotNull(teacherDAO);
        assertNotNull(makeAppointment);

        Teacher teacher= Teacher.builder()
                .id(1L)
                .username("teacher")
                .schedules(Collections.singletonList(RegularSchedule.builder()
                        .dayOfWeek(LocalDate.now().getDayOfWeek())
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .build()))
                .build();

        Student student= Student.builder()
                .id(1L)
                .username("27583")
                .build();

        when(studentDAO.findByUsername("27583")).thenReturn(Optional.of(student));
        when(teacherDAO.findByUsername("teacher")).thenReturn(Optional.of(teacher));


        Appointment appointment= Appointment.builder()
                .startHour(LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(8,0)
                ))
                .student(student)
                .teacher(teacher)
                .build();

        when(appointmentDAO.save(appointment)).thenReturn(appointment);

        Appointment appointment1=makeAppointment.makeAppointment(appointment);
        assertNotNull(appointment1);

        Appointment finalAppointment1 = appointment;
        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(finalAppointment1));


        appointment= Appointment.builder()
                .startHour(LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(8,30)
                ))
                .student(student)
                .teacher(teacher)
                .build();

        when(appointmentDAO.save(appointment)).thenReturn(appointment);

        appointment1=makeAppointment.makeAppointment(appointment);
        assertNotNull(appointment1);

        Appointment finalAppointment = Appointment.builder()
                .startHour(LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(8,30)
                ))
                .student(student)
                .teacher(teacher)
                .build();
        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(finalAppointment));
    }


    @Test
    void makeAppointmentExtraordinarySchedule(){

        Course course=Course.builder().name("course1").build();
        Student student1=Student.builder().username("27583").build();
        course.addStudent(student1);

        ExtraordinarySchedule schedule1=ExtraordinarySchedule.builder()
                .dayOfWeek(LocalDate.now().getDayOfWeek())
                .course(course)
                .startPeriod(LocalDate.now())
                .endPeriod(LocalDate.now().plusWeeks(1))
                .startTime(LocalTime.of(8,0))
                .endTime(LocalTime.of(10,0)).build();

        ExtraordinarySchedule schedule2=ExtraordinarySchedule.builder()
                .dayOfWeek(LocalDate.now().getDayOfWeek())
                .course(course)
                .startPeriod(LocalDate.now().plusWeeks(1))
                .endPeriod(LocalDate.now().plusWeeks(2))
                .startTime(LocalTime.of(16,0))
                .endTime(LocalTime.of(18,0)).build();

        AbstractSchedule schedule3=RegularSchedule.builder()
                .dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek())
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(12,0)).build();


        Teacher teacher=Teacher.builder()
                    .username("teacher1")
                    .build();

        teacher.addSchedule(schedule1)
        .addSchedule(schedule2)
        .addSchedule(schedule3);

        Appointment appointment1= Appointment.builder()
                .student(student1)
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)))
                .teacher(teacher)
                .build();

        Student student2=Student.builder().username("student2").build();

        Appointment appointment2= Appointment.builder()
                .student(student2)
                .startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,30)))
                .teacher(teacher)
                .build();

        when(studentDAO.findByUsername("27583")).thenReturn(Optional.of(student1));
        when(teacherDAO.findByUsername("teacher1")).thenReturn(Optional.of(teacher));

        when(appointmentDAO.save(appointment1)).thenReturn(appointment1);
        when(appointmentDAO.save(appointment2)).thenReturn(appointment2);

        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course));

        assertNotNull(makeAppointment.makeAppointment(appointment1));
        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointment2));

        when(studentDAO.findByUsername("student2")).thenReturn(Optional.of(student2));

    }
    @Test
    void testInvalidAppointmentsExtraordinarySchedule(){
        LocalDate date=LocalDate.now().plusWeeks(1);

        Course course= Course.builder().name("course1").build();

        List<AbstractSchedule> scheduleList= Collections.singletonList(
                ExtraordinarySchedule.builder()
                        .course(course).startPeriod(date)
                        .dayOfWeek(date.getDayOfWeek())
                        .endPeriod(date.plusDays(5)).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(12, 0)).build());

        Teacher teacher=Teacher.builder()
                .username("teacher1")
                .build();

        scheduleList.forEach(teacher::addSchedule);

        Student student=Student.builder().username("student1").build();

        when(teacherDAO.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(studentDAO.findByUsername("student1")).thenReturn(Optional.of(student));

        Appointment appointment= Appointment.builder()
                .startHour(LocalDateTime.of(date,LocalTime.of(8,0)))
                .teacher(teacher)
                .student(student)
                .build();

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointment));

        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course));

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointment));

        course.addStudent(student);

        when(appointmentDAO.save(appointment)).thenReturn(appointment);
        assertNotNull(makeAppointment.makeAppointment(appointment));


        Appointment appointment2= Appointment.builder()
                .startHour(LocalDateTime.of(date,LocalTime.of(8,30)))
                .teacher(teacher)
                .student(student)
                .build();

        when(appointmentDAO.save(appointment2)).thenReturn(appointment2);
        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointment2));

    }

    @Test
    void testInvalidAppointmentsRegularSchedule(){

        LocalDate date=LocalDate.now();

        List<AbstractSchedule> scheduleList= Arrays.asList(
                RegularSchedule.builder().dayOfWeek(date.getDayOfWeek()).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(10, 0)).build(),
                RegularSchedule.builder().dayOfWeek(date.plusDays(2).getDayOfWeek()).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(10, 0)).build()
        );

        Teacher teacher=Teacher.builder()
                .username("teacher1")
                .build();

        scheduleList.forEach(teacher::addSchedule);

        Student student=Student.builder().username("student1").build();

        when(teacherDAO.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(studentDAO.findByUsername("student1")).thenReturn(Optional.of(student));

        Appointment appointmentWithoutTeacher= Appointment.builder()
                .startHour(LocalDateTime.of(date,LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date,LocalTime.of(8,30)))
                .teacher(Teacher.builder().username("nonexistent").build()).student(student).build();

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointmentWithoutTeacher));

        Appointment appointmentBeforeADay= Appointment.builder()
                .startHour(LocalDateTime.of(date,LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date,LocalTime.of(8,30)))
                .teacher(teacher).student(student).build();

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointmentBeforeADay));

        Appointment appointment2Weeks= Appointment.builder()
                .startHour(LocalDateTime.of(date.plusWeeks(3),LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date.plusWeeks(3),LocalTime.of(8,30)))
                .teacher(teacher).student(student).build();

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointment2Weeks));

        Appointment appointmentValid1= Appointment.builder()
                .startHour(LocalDateTime.of(date.plusDays(2),LocalTime.of(8,0)))
                .expectedEndTime(LocalDateTime.of(date.plusDays(2),LocalTime.of(8,30)))
                .teacher(teacher).student(student).build();

        when(appointmentDAO.save(appointmentValid1)).thenReturn(appointmentValid1);
        assertNotNull(makeAppointment.makeAppointment(appointmentValid1));

        Appointment appointmentValid2= Appointment.builder()
                .startHour(LocalDateTime.of(date.plusDays(2),LocalTime.of(8,30)))
                .expectedEndTime(LocalDateTime.of(date.plusDays(2),LocalTime.of(9,0)))
                .teacher(teacher).student(student).build();

        when(appointmentDAO.save(appointmentValid2)).thenReturn(appointmentValid2);
        assertNotNull(makeAppointment.makeAppointment(appointmentValid2));

        Appointment appointmentMaxPerWeek= Appointment.builder()
                .startHour(LocalDateTime.of(date.plusDays(2),LocalTime.of(9,30)))
                .expectedEndTime(LocalDateTime.of(date.plusDays(2),LocalTime.of(10,0)))
                .teacher(teacher).student(student).build();

        assertThrows(ResponsesException.class,()->makeAppointment.makeAppointment(appointmentMaxPerWeek));

    }


}