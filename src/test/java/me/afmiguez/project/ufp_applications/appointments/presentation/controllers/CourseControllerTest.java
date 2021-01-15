package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.AssociateStudentsToCourseDTO;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.CourseResponseDTO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform.ModelToDTOFactory;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignStudentsToCourse;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetAllCourses;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetCourseByName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CourseController.class)
//ignore security configurations
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAllCourses getAllCourses;
    @MockBean
    private GetCourseByName getCourseByName;
    @MockBean
    private AssignStudentsToCourse assignStudentsToCourse;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void getAllCourses() {

        List<Course> courses= Arrays.asList(
                Course.builder().id(1L).name("course1").build(),
                Course.builder().id(2L).name("course2").build()
        );

        when(getAllCourses.getAllCourses()).thenReturn(courses);


        mockMvc.perform(get("/api/course")).andExpect(status().isOk());

    }

    //@PutMapping
    @SneakyThrows
    @Test
    void associateStudentsToCourse(){

        ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();

        List<Student> students=Arrays.asList(
                Student.builder().username("student1").build(),
                Student.builder().username("student2").build(),
                Student.builder().username("student3").build()
        );

        Course course= Course.builder().name("course1").build();

        Course course2= Course.builder().name("course2").build();

        when(assignStudentsToCourse.assignStudentsToCourse(course,students)).thenReturn(course);

        AssociateStudentsToCourseDTO associateStudentsToCourseDTO= AssociateStudentsToCourseDTO.builder()
                .course(CourseResponseDTO.builder().name("course1").build()).students(students.stream().map(modelToDTOFactory::convertStudentToDTO).collect(Collectors.toList())).build();

        mockMvc.perform(put("/api/course").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(associateStudentsToCourseDTO))).andExpect(status().isOk());

        when(assignStudentsToCourse.assignStudentsToCourse(course2,students)).thenThrow(new ResponsesException(HttpStatus.BAD_REQUEST,"bad request"));

        AssociateStudentsToCourseDTO associateStudentsToCourseDTO2= AssociateStudentsToCourseDTO.builder()
                .course(CourseResponseDTO.builder().name("course2").build()).students(students.stream().map(modelToDTOFactory::convertStudentToDTO).collect(Collectors.toList())).build();

        mockMvc.perform(put("/api/course").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(associateStudentsToCourseDTO2))).andExpect(status().isBadRequest());

    }

    //@GetMapping("/{courseName}")
    @SneakyThrows
    @Test
    void getCourseByName(){
        Course course= Course.builder().name("course1").build();

        when(getCourseByName.findByName("course1")).thenReturn(course);

        mockMvc.perform(get("/api/course/course1")).andExpect(status().isOk());

        when(getCourseByName.findByName("course2")).thenThrow(new ResponsesException(HttpStatus.NOT_FOUND,"no course found"));

        mockMvc.perform(get("/api/course/course2")).andExpect(status().isNotFound());

    }
}