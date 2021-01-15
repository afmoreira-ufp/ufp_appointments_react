package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class RegularScheduleDTO implements ScheduleDTO{
    private Long id;
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern = "HH:mm",shape = JsonFormat.Shape.STRING)
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm",shape = JsonFormat.Shape.STRING)
    private LocalTime endTime;
}
