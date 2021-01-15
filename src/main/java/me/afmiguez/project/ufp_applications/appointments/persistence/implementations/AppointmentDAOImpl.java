package me.afmiguez.project.ufp_applications.appointments.persistence.implementations;

import me.afmiguez.project.ufp_applications.appointments.persistence.implementations.spring.AppointmentRepository;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentDAOImpl implements AppointmentDAO {

    private final AppointmentRepository appointmentRepository;

    public AppointmentDAOImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment delete(Long id) {
        Optional<Appointment> optionalAppointment=appointmentRepository.findById(id);
        if(optionalAppointment.isPresent()) {
            appointmentRepository.deleteById(id);
            return optionalAppointment.get();
        }
        return null;
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}
