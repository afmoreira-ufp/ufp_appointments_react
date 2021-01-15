package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignStudentsToCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AssignStudentsToCourseImpl.class)
class AssignStudentsToCourseImplTest {

    @Autowired
    private AssignStudentsToCourse assignStudentsToCourse;

    @MockBean
    private CourseDAO courseDAO;
    @MockBean
    private StudentDAO studentDAO;

    @Test
    void assignStudentsToCourse() {

        Course course= Course.builder()
                .name("course1")
                .build();
        Student student1=Student.builder().username("student1").build();
        Student student2=Student.builder().username("student2").build();
        Student student3=Student.builder().username("nonExistentStudent").build();

        List<Student> studentList= Arrays.asList(
                student1,
                student2,
                student3
        );

        when(courseDAO.findByName("course1")).thenReturn(Optional.of(course));
        when(courseDAO.save(course)).thenReturn(course);
        when(studentDAO.findByUsername("student1")).thenReturn(Optional.of(student1));
        when(studentDAO.findByUsername("student2")).thenReturn(Optional.of(student2));
        when(studentDAO.save(student3)).thenReturn(student3);

        Course courseFromService=assignStudentsToCourse.assignStudentsToCourse(course,studentList);

        assertNotNull(courseFromService);
        assertEquals(3,courseFromService.getStudents().size());

        assertThrows(ResponsesException.class,()->assignStudentsToCourse.assignStudentsToCourse(Course.builder().name("inexistentCourse").build(),studentList));


    }

}