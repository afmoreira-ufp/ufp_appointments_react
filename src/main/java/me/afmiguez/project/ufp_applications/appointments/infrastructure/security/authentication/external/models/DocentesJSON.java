package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DocentesJSON {
    @JsonProperty("Docentes")
    private List<DocenteJSON> docentes=new ArrayList<>();
}
