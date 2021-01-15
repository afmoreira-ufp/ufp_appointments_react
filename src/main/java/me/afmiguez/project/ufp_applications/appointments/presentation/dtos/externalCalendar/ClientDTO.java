package me.afmiguez.project.ufp_applications.appointments.presentation.dtos.externalCalendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private String username;
    private String clientName;
}
