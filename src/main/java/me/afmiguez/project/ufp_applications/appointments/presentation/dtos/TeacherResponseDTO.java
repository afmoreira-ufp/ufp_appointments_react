package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TeacherResponseDTO {
    private final Long id;
    private final String username;
    private final List<ScheduleDTO> schedules;
    private final List<CourseDTO> courses;
    private final List<AppointmentDTO> appointments;

}
