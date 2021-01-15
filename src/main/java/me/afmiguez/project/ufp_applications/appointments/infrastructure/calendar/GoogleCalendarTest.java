package me.afmiguez.project.ufp_applications.appointments.infrastructure.calendar;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"test"})
@Service
public class GoogleCalendarTest implements CalendarService {
    @Override
    public boolean createGCalEvent(Appointment appointment) {
        return true;
    }

    @Override
    public boolean removeGCalEvent(Appointment appointment) {
        return true;
    }
}
