package me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
}
