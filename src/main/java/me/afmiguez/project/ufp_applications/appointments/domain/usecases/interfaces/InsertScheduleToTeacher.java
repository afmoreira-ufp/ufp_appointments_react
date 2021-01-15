package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractSchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

import java.util.List;

public interface InsertScheduleToTeacher {

    Teacher insertSchedule(Long id, List<AbstractSchedule> scheduleDTOS);
}
