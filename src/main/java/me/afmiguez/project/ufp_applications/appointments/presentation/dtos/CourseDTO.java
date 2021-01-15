package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    @Builder.Default
    private List<String> students=new ArrayList<>();
}
