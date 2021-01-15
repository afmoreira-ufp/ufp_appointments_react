package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.InsertScheduleToTeacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = InsertScheduleToTeacherImpl.class)
class InsertScheduleToTeacherImplTest {

    @MockBean
    private TeacherDAO teacherDAO;
    @Autowired
    private InsertScheduleToTeacher insertScheduleToTeacher;
    @MockBean
    private CourseDAO courseDAO;

    @Test
    void insertSchedule() {

        Teacher teacher=Teacher.builder()
                .id(1L)
                .schedules(new ArrayList<>())
                .build();

        Course course= Course.builder()
                .name("course1")
                .build();

        List<AbstractSchedule> scheduleList= Arrays.asList(
                RegularSchedule.builder()
                        .dayOfWeek(LocalDate.now().getDayOfWeek())
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .build(),
                ExtraordinarySchedule.builder()
                        .dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek())
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .startPeriod(LocalDate.now().minusDays(1))
                        .endPeriod(LocalDate.now().plusDays(1))
                        .course(course)
                        .build()
        );

        when(teacherDAO.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherDAO.save(teacher)).thenReturn(teacher);
        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course));

        Teacher teacherFromService= insertScheduleToTeacher.insertSchedule(1L,scheduleList);
        assertNotNull(teacherFromService);
        assertEquals(2,teacherFromService.getSchedules().size());

        assertThrows(ResponsesException.class,()->insertScheduleToTeacher.insertSchedule(2L,scheduleList));


    }
}