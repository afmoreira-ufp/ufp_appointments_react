package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignCoursesToTeacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AssignCoursesToTeacherImpl.class)
class AssignCoursesToTeacherImplTest {

    @MockBean
    private CourseDAO courseDAO;
    @MockBean
    private TeacherDAO teacherDAO;
    @Autowired
    private AssignCoursesToTeacher coursesToTeacher;

    @Test
    void assignCourses() {

        assertNotNull(courseDAO);
        assertNotNull(teacherDAO);
        assertNotNull(coursesToTeacher);

        Teacher teacher1= Teacher.builder()
                .id(1L)
                .build();

        Teacher teacher2= Teacher.builder()
                .id(2L)
                .build();

        Course course1= Course.builder()
                .name("course1")
                .build();

        Course course2= Course.builder()
                .name("course2")
                .build();

        Course course3= Course.builder()
                .name("course3")
                .build();

        Course course4= Course.builder()
                .name("course4")
                .teachers(new ArrayList<>())
                .build();

        when(teacherDAO.findById(1L)).thenReturn(Optional.of(teacher1));
        when(teacherDAO.findById(2L)).thenReturn(Optional.of(teacher2));

        when(teacherDAO.save(teacher1)).thenReturn(teacher1);
        when(teacherDAO.save(teacher2)).thenReturn(teacher2);

        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course1));
        when(courseDAO.findByName("course2")).thenReturn(Optional.of(course2));
        when(courseDAO.findByName("course3")).thenReturn(Optional.of(course3));
        when(courseDAO.findByName("course4")).thenReturn(Optional.of(course4));

        Teacher optionalTeacher=coursesToTeacher.assignCourses(1L, Arrays.asList(course1,course2,course3));
        assertNotNull(optionalTeacher);

        optionalTeacher=coursesToTeacher.assignCourses(2L, Arrays.asList(course1,course3));
        assertNotNull(optionalTeacher);

        assertEquals(2,course1.getTeachers().size());
        assertEquals(1,course2.getTeachers().size());
        assertEquals(2,course3.getTeachers().size());
        assertEquals(0,course4.getTeachers().size());

        assertThrows(ResponsesException.class,()->coursesToTeacher.assignCourses(3L, Arrays.asList(course1,course3)));

    }

    @Test
    void assignCoursesByTeacherUsername(){
        assertNotNull(courseDAO);
        assertNotNull(teacherDAO);
        assertNotNull(coursesToTeacher);

        Teacher teacher1= Teacher.builder()
                .id(1L)
                .username("teacher1")
                .build();

        Teacher teacher2= Teacher.builder()
                .id(2L)
                .username("teacher2")
                .build();

        Course course1= Course.builder()
                .name("course1")
                .build();

        Course course2= Course.builder()
                .name("course2")
                .build();

        Course course3= Course.builder()
                .name("course3")
                .build();

        Course course4= Course.builder()
                .name("course4")
                .teachers(new ArrayList<>())
                .build();

        when(teacherDAO.findByUsername("teacher1")).thenReturn(Optional.of(teacher1));
        when(teacherDAO.findByUsername("teacher2")).thenReturn(Optional.of(teacher2));

        when(teacherDAO.save(teacher1)).thenReturn(teacher1);
        when(teacherDAO.save(teacher2)).thenReturn(teacher2);

        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course1));
        when(courseDAO.findByName("course2")).thenReturn(Optional.of(course2));
        when(courseDAO.findByName("course3")).thenReturn(Optional.of(course3));
        when(courseDAO.findByName("course4")).thenReturn(Optional.of(course4));

        Teacher optionalTeacher=coursesToTeacher.assignCourses("teacher1", Arrays.asList(course1,course2,course3));
        assertNotNull(optionalTeacher);

        optionalTeacher=coursesToTeacher.assignCourses("teacher2", Arrays.asList(course1,course3));
        assertNotNull(optionalTeacher);

        assertEquals(2,course1.getTeachers().size());
        assertEquals(1,course2.getTeachers().size());
        assertEquals(2,course3.getTeachers().size());
        assertEquals(0,course4.getTeachers().size());

        assertThrows(ResponsesException.class,()->coursesToTeacher.assignCourses("teacher3", Arrays.asList(course1,course3)));
    }
}