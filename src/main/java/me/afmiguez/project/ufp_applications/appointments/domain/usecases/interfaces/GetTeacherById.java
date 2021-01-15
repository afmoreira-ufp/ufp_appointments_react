package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

import java.util.Optional;

public interface GetTeacherById {
    Optional<Teacher> getTeacherById(Long id);
}
