package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssociateStudentsToCourseDTO {
    private CourseResponseDTO course;
    private List<StudentDTO> students;
}
