package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("regularSchedule")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ExtraordinarySchedule extends AbstractSchedule{

    @OneToOne
    @ToString.Exclude
    private Course course;
    private LocalDate startPeriod;
    private LocalDate endPeriod;

    @Override
    public boolean contains(Appointment appointment) {
        if(
                containsPeriod(appointment)
        ){
            return super.contains(appointment);
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return super.isValid()&& ( this.getStartPeriod().isBefore(this.getEndPeriod()) || this.getStartPeriod().equals(this.getEndPeriod()) );
    }

    public boolean containsPeriod(Appointment appointment) {
        LocalDate appointmentDate=appointment.getStartHour().toLocalDate();
        return (getStartPeriod().equals(appointmentDate) || getStartPeriod().isBefore(appointmentDate))
                &&
                (getEndPeriod().equals(appointmentDate) || getEndPeriod().isAfter(appointmentDate));
    }
}
