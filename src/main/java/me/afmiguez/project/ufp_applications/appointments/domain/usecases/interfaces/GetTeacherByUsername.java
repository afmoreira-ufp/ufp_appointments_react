package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

public interface GetTeacherByUsername {
    Teacher getTeacherByUsername(String username);
}
