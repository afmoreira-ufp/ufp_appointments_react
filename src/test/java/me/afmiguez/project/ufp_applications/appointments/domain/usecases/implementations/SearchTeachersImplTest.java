package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.SearchTeachers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SearchTeachersImpl.class)
class SearchTeachersImplTest {

    @Autowired
    private SearchTeachers searchTeachers;

    @MockBean
    private TeacherDAO teacherDAO;

    @Test
    void findAllTeachers() {

        List<Teacher> teacherList=new ArrayList<>();
        teacherList.add(Teacher.builder().id(1L).build());
        teacherList.add(Teacher.builder().id(2L).build());

        when(teacherDAO.findAll()).thenReturn(teacherList);

        assertEquals(2,searchTeachers.findAllTeachers().size());

    }
}