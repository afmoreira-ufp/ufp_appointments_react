package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentDAO {
    Appointment delete(Long id);
    Optional<Appointment> findById(Long id);

    List<Appointment> findAll();

    Appointment save(Appointment appointment);
}
