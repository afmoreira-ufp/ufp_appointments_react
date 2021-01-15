package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.AppointmentCreateDTO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CancelAppointment;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.MakeAppointment;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class)
//ignore security configurations
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MakeAppointment makeAppointment;
    @MockBean
    private CancelAppointment cancelAppointment;

    @Autowired
    private ObjectMapper objectMapper;
    private final ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();

    @SneakyThrows
    @Test
    void createAppointment() {
        AppointmentCreateDTO appointmentCreateDTO= AppointmentCreateDTO.builder()
                .startHour(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0)))
                .build();

        Appointment appointment=modelToDTOFactory.convertAppointmentToModel(appointmentCreateDTO);
        appointment.setId(1L);

        when(makeAppointment.makeAppointment
                (modelToDTOFactory.convertAppointmentToModel(appointmentCreateDTO)))
                .thenReturn(appointment);

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentCreateDTO))).
                andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void cancelAppointment() {

        when(cancelAppointment.cancelAppointment(1L,"student")).thenReturn(Appointment.builder().student(Student.builder().username("student").build()).startHour(LocalDateTime.now()).build());

        mockMvc.perform(delete("/api/appointment/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        when(cancelAppointment.cancelAppointment(2L,"")).thenThrow(new ResponsesException(HttpStatus.BAD_REQUEST,"exception"));

        mockMvc.perform(delete("/api/appointment/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}