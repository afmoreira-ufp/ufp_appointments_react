package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class AppointmentDTO {
    private final Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",shape = JsonFormat.Shape.STRING)
    private final LocalDateTime startHour;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",shape = JsonFormat.Shape.STRING)
    private final LocalDateTime expectedEndHour;
    private final String teacherUser;
    private final String studentUser;
}
