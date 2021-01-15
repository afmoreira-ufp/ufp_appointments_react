package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DocenteDetailsJson {
    private String email;
    private List<ScheduleSoap> schedule;
    private String name;
    private LocalDate last_update;
}
