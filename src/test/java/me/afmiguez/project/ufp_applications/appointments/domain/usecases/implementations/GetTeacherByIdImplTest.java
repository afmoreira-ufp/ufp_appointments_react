package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetTeacherById;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GetTeacherByIdImpl.class)
class GetTeacherByIdImplTest {

    @Autowired
    private GetTeacherById getTeacherById;

    @MockBean
    private TeacherDAO teacherDAO;

    @Test
    void getTeacherById() {

        Teacher teacher1=Teacher.builder().username("teacher1").build();

        
        when(teacherDAO.findById(1L)).thenReturn(Optional.of(teacher1));

        assertTrue(getTeacherById.getTeacherById(1L).isPresent());
        assertThrows(ResponsesException.class,()->getTeacherById.getTeacherById(2L));


    }
}