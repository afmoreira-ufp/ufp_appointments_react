package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Matricula {
    @JsonProperty("Grau")
    private String grau;
    @JsonProperty("Curso")
    private String curso;
}
