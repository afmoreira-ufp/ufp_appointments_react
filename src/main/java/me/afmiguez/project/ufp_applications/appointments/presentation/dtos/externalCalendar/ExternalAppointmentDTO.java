package me.afmiguez.project.ufp_applications.appointments.presentation.dtos.externalCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalAppointmentDTO {
    private final static String pattern="yyyy-MM-dd HH:mm:ss";
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = pattern)
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = pattern)
    private LocalDateTime expectedEndTime;
    private EmployeeDTO employee;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ClientDTO client;
}

