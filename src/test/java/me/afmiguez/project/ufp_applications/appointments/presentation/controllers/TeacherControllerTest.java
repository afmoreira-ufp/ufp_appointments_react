package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.*;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.*;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform.ModelToDTOFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TeacherController.class)
//ignore security configurations
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AssignCoursesToTeacher assignCoursesToTeacher;
    @MockBean
    private InsertScheduleToTeacher insertScheduleToTeacher;
    @MockBean
    private ListAppointments listAppointments;
    @MockBean
    private SearchTeachers searchTeachers;
    @MockBean
    private GetTeacherById getTeacherById;
    @MockBean
    private GetTeacherByUsername getTeacherByUsername;
    @MockBean
    private ListTeacherAppointmentsFromNow listTeacherAppointmentsFromNow;
    @MockBean
    private RemoveExtraSchedulesFromIdList removeExtraSchedulesFromIdList;

    @Autowired
    private ObjectMapper objectMapper;
    private final ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();


    //@GetMapping(value = "/{id}/course")
    @SneakyThrows
    @Test
    void getTeacherCourses(){
        List<Course> courses=Arrays.asList(
                Course.builder().name("course1").build(),
                Course.builder().name("course2").build(),
                Course.builder().name("course3").build()
        );

        Teacher teacher=Teacher.builder().courses(courses).build();

        when(getTeacherById.getTeacherById(1L)).thenReturn(Optional.of(teacher));

        String jsonResponse=mockMvc.perform(get("/api/teacher/1/course")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> coursesFromResponse=objectMapper.readValue(jsonResponse,List.class);

        assertEquals(3,coursesFromResponse.size());

        String invalidResponse=mockMvc.perform(get("/api/teacher/2/course")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(invalidResponse.isEmpty());
    }

    //@GetMapping("/{username}/nextAppointments")
    @SneakyThrows
    @Test
    void listNextAppointments(){

        List<Appointment> listAppointments=Arrays.asList(
            Appointment.builder().teacher(Teacher.builder().build()).student(Student.builder().build()).startHour(LocalDateTime.now()).build(),
            Appointment.builder().teacher(Teacher.builder().build()).student(Student.builder().build()).startHour(LocalDateTime.now().plusHours(1)).build()
        );

        when(listTeacherAppointmentsFromNow.listAppointmentsFromNow("teacher1")).thenReturn(listAppointments);

        String jsonResponse=mockMvc.perform(get("/api/teacher/teacher1/nextAppointments")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<AppointmentDetailsDTO> appointmentsFromResponse=objectMapper.readValue(jsonResponse,List.class);
        assertEquals(2,appointmentsFromResponse.size());

        String jsonInvalidResponse=mockMvc.perform(get("/api/teacher/teacher2/nextAppointments")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertTrue(objectMapper.readValue(jsonInvalidResponse,List.class).isEmpty());



    }

    //@PutMapping(value = "/name/{username}/course")
    @SneakyThrows
    @Test
    void insertCourseToTeacherByUsername(){

        Teacher teacher=Teacher.builder().username("teacher1").build();

        List<Course> courses=Collections.singletonList(Course.builder().name("course1").build()) ;

        when(assignCoursesToTeacher.assignCourses("teacher1",courses)).thenReturn(teacher);

        mockMvc.perform(put("/api/teacher/name/teacher1/course").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(courses))).andExpect(status().isOk());

        when(assignCoursesToTeacher.assignCourses("teacher2",null)).thenThrow(new ResponsesException(HttpStatus.BAD_REQUEST,"bad request"));
        mockMvc.perform(put("/api/teacher/name/teacher2/course").contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isBadRequest());

    }

    //@GetMapping(value="/name/{username}")
    @SneakyThrows
    @Test
    void getTeacherByUsername(){

        Teacher teacher=Teacher.builder().username("teacher1").build();
        when(getTeacherByUsername.getTeacherByUsername("teacher1")).thenReturn(teacher);

        mockMvc.perform(get("/api/teacher/name/teacher1")).andExpect(status().isOk());

        when(getTeacherByUsername.getTeacherByUsername("teacher2")).thenThrow(new ResponsesException(HttpStatus.NOT_FOUND,"not found"));
        mockMvc.perform(get("/api/teacher/name/teacher2")).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllTeachers() {

        List<Teacher> teachers= Arrays.asList(
          Teacher.builder().id(1L).courses(Arrays.asList(
                  Course.builder().name("course1").build(),
                  Course.builder().name("course2").build()
          )).appointments(Arrays.asList(
                  Appointment.builder().startHour(LocalDateTime.now()).build(),
                  Appointment.builder().startHour(LocalDateTime.now().plusHours(1)).build()
          )).schedules(Arrays.asList(
                  RegularSchedule.builder().dayOfWeek(LocalDate.now().getDayOfWeek()).build(),
                  ExtraordinarySchedule.builder().course(Course.builder().id(1L).name("course1").build()).dayOfWeek(LocalDate.now().getDayOfWeek()).build()
          )).build(),
          Teacher.builder().id(2L).build()
        );

        when(searchTeachers.findAllTeachers()).thenReturn(teachers);
        mockMvc.perform(get("/api/teacher").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @SneakyThrows
    @Test
    void insertSchedule() {
        List<ScheduleDTO> scheduleDTOS=Arrays.asList(
                RegularScheduleDTO.builder().dayOfWeek(LocalDate.now().getDayOfWeek()).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build(),
                RegularScheduleDTO.builder().dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek()).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build()
        );

        when(insertScheduleToTeacher.insertSchedule(1L,scheduleDTOS.stream().map(modelToDTOFactory::convertScheduleToModel).collect(Collectors.toList()))).thenReturn(Teacher.builder().build());

        mockMvc.perform(patch("/api/teacher/1/regular").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(scheduleDTOS))).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void insertExtraordinarySchedule() {
        List<ExtraScheduleDTO> scheduleDTOS=Arrays.asList(
                ExtraScheduleDTO.builder().courseDTO(CourseDTO.builder().id(1L).build()).dayOfWeek(LocalDate.now().getDayOfWeek()).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build(),
                ExtraScheduleDTO.builder().courseDTO(CourseDTO.builder().id(1L).build()).dayOfWeek(LocalDate.now().plusDays(1).getDayOfWeek()).startTime(LocalTime.of(8,0)).endTime(LocalTime.of(10,0)).build()
        );

        when(insertScheduleToTeacher.insertSchedule(1L,scheduleDTOS.stream().map(modelToDTOFactory::convertScheduleToModel).collect(Collectors.toList()))).thenReturn(Teacher.builder().build());

        mockMvc.perform(patch("/api/teacher/1/extraordinary").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(scheduleDTOS))).andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void listAppointments() {
        List<AppointmentCreateDTO> appointments=Arrays.asList(
                AppointmentCreateDTO.builder().startHour(LocalDateTime.now()).build(),
                AppointmentCreateDTO.builder().startHour(LocalDateTime.now().plusHours(1)).build()
        );
        when(listAppointments.listAppointments(1L)).thenReturn(appointments.stream().map(modelToDTOFactory::convertAppointmentToModel).collect(Collectors.toList()));

        mockMvc.perform(get("/api/teacher/1/appointment").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(appointments))).andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void insertCourseToTeacher() {
        List<CourseDTO> courses=Arrays.asList(
                CourseDTO.builder().name("course1").build(),
                CourseDTO.builder().name("course2").build()
        );

        when(this.assignCoursesToTeacher.assignCourses(1L,courses.stream().map(modelToDTOFactory::convertCourseDTOToModel).collect(Collectors.toList()))).thenReturn(Teacher.builder().build());

        String requestJson=objectMapper.writeValueAsString(courses);
        mockMvc.perform(put("/api/teacher/1/course").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void getTeacherById(){
        Teacher teacher=Teacher.builder().username("teacher").build();

        when(getTeacherById.getTeacherById(1L)).thenReturn(Optional.of(teacher));
        when(getTeacherById.getTeacherById(2L)).thenThrow(new ResponsesException(HttpStatus.NOT_FOUND,""));


        mockMvc.perform(get("/api/teacher/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/teacher/2")).andExpect(status().isNotFound());
    }
}