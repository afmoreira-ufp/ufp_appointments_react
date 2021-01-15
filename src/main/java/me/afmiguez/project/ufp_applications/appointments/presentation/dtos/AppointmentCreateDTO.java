package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = JsonFormat.Shape.STRING)
    private LocalDateTime startHour;
    private String studentUsername;
    private String teacherUsername;
}
