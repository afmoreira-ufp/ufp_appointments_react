package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="schedule_type")
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractSchedule implements Schedule{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private Teacher teacher;

    public boolean contains(Appointment appointment) {
        if(!this.getDayOfWeek().equals(appointment.getStartHour().toLocalDate().getDayOfWeek())){
            return false;
        }
        LocalTime appointmentStartTime=appointment.getStartHour().toLocalTime();
        LocalTime appointmentEndTime=appointment.getExpectedEndTime().toLocalTime();
        return (this.getStartTime().isBefore(appointmentStartTime) || this.getStartTime().equals(appointmentStartTime))
                &&
                (this.getEndTime().isAfter(appointmentEndTime) || this.getEndTime().equals(appointmentEndTime));
    }

    public boolean isValid(){
        return this.getStartTime().isBefore(this.getEndTime());
    }
}
