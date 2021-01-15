package me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseTest {

    @Autowired
    private TeacherDAO teacherDAO;
    @Autowired
    private AppointmentDAO appointmentDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private StudentDAO studentDAO;

    @Test
    @Transactional
    public void createEntities(){

        Arrays.asList(
                Course.builder()
                .name("course1")
                .build(),
                Course.builder()
                .name("course2")
                .build(),
                Course.builder()
                .name("course3")
                .build()
        ).forEach(course -> courseDAO.save(course));

        Student student=Student.builder().username("student1").build();

        studentDAO.save(student);

        Teacher teacher= Teacher.builder()
                .username("teacher1")
                .build();

        Arrays.asList(
                RegularSchedule.builder().dayOfWeek(LocalDate.now().getDayOfWeek())
                        .startTime(LocalTime.of(8,0))
                        .endTime(LocalTime.of(10,0)).build(),
                RegularSchedule.builder().dayOfWeek(LocalDate.now().getDayOfWeek())
                        .startTime(LocalTime.of(14,0))
                        .endTime(LocalTime.of(16,0)).build(),
                ExtraordinarySchedule.builder().dayOfWeek(
                        LocalDate.now().getDayOfWeek())
                        .startPeriod(LocalDate.now().plusWeeks(1))
                        .endPeriod(LocalDate.now().plusWeeks(2))
                        .startTime(LocalTime.of(18,0))
                        .endTime(LocalTime.of(20,0)).build()
        ).forEach(teacher::addSchedule);

        Arrays.asList(
                Appointment.builder().startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)))
                        .student(student).build(),
                Appointment.builder().startHour(LocalDateTime.of(LocalDate.now(),LocalTime.of(14,0)))
                        .student(student).build(),
                Appointment.builder().startHour(LocalDateTime.of(LocalDate.now().plusWeeks(1),LocalTime.of(18,10)))
                        .student(student).build()
        ).forEach(appointment -> {
            assertTrue(teacher.canAttendAppointment(appointment));
            teacher.addAppointment(appointment);
        });

        Optional<Integer> optional=Optional.of(1);
        optional.orElseThrow(() -> new RuntimeException(""));

        teacherDAO.save(teacher);

        List<Teacher> teacherList= teacherDAO.findAll();
        assertEquals(1,teacherList.size());
        Teacher teacher1=teacherList.get(0);
        assertNotNull(teacher1);
        assertEquals(3,teacher1.getAppointments().size());
        assertEquals(3,appointmentDAO.findAll().size());
        assertEquals(3,courseDAO.findAll().size());


        Optional<Course> optionalCourse=courseDAO.findById(1L);
        assertTrue(optionalCourse.isPresent());

        Optional<Teacher>optionalTeacher= teacherDAO.findById(2L);
        assertTrue(optionalTeacher.isPresent());
        Teacher teacheFromDAO=optionalTeacher.get();
        Course course=optionalCourse.get();

        teacheFromDAO.addCourse(course);

        teacherDAO.save(teacheFromDAO);
        courseDAO.save(course);

        optionalCourse=courseDAO.findById(1L);
        assertTrue(optionalCourse.isPresent());
        assertEquals(1,optionalCourse.get().getTeachers().size());


        Optional<Appointment> optionalAppointment=appointmentDAO.findById(1L);
        assertTrue(optionalAppointment.isPresent());

        assertNotNull(appointmentDAO.delete(1L));

        assertTrue(studentDAO.findById(1L).isPresent());

    }
}