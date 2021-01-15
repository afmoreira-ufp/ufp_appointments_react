package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

import java.util.List;

public interface RemoveExtraSchedulesFromIdList {
    Teacher removeExtraSchedules(String username, List<Long> ids);
}
