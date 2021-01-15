package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class StudentJSON {
    @JsonProperty("Nome")
    private String nome;
    @JsonProperty("Numero")
    private String numero;
    @JsonProperty("Matricula")
    private List<Matricula> matricula;
}
