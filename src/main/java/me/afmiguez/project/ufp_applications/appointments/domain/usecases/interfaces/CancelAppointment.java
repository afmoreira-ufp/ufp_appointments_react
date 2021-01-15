package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;

public interface CancelAppointment {
    Appointment cancelAppointment(Long id, String actualUser);
}
