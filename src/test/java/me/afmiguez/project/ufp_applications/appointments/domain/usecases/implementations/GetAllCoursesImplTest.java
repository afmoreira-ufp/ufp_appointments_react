package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetAllCourses;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = GetAllCoursesImpl.class)
class GetAllCoursesImplTest {

    @Autowired
    private GetAllCourses getAllCourses;

    @MockBean
    private CourseDAO courseDAO;

    @Test
    void getAllCourses() {
        List<Course> courses=Arrays.asList(
                Course.builder().name("course1").build(),
                Course.builder().name("course2").build(),
                Course.builder().name("course3").build()
        );

        when(courseDAO.findAll()).thenReturn(courses);

        assertEquals(3,getAllCourses.getAllCourses().size());

    }
}