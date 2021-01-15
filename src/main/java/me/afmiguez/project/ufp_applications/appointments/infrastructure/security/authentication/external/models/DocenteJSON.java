package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocenteJSON {
    @JsonProperty("Docente")
    private String docente;
    @JsonProperty("Sigla")
    private String sigla;
}
