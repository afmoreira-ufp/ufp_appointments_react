package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;

public interface CalendarService {
    boolean createGCalEvent(Appointment appointment);

    boolean removeGCalEvent(Appointment appointment);
}
