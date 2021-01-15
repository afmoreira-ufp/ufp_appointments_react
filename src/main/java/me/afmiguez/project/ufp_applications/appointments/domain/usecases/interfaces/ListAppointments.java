package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;

import java.util.List;

public interface ListAppointments {

    List<Appointment> listAppointments(Long teacherId);
}
