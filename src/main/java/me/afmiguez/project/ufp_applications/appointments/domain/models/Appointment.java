package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private Teacher teacher;

    @ManyToOne
    @ToString.Exclude
    private Student student;

    private LocalDateTime startHour;
    private LocalDateTime expectedEndTime;

    public Appointment(Long id, Teacher teacher, Student student, LocalDateTime startHour, LocalDateTime expectedEndTime) {
        this.id = id;
        this.teacher = teacher;
        this.student = student;
        this.startHour = startHour;
        this.expectedEndTime = expectedEndTime!=null?expectedEndTime:startHour.plusMinutes(30);
    }

    public boolean overlaps(Appointment other) {

        LocalDateTime appointmentStartTime=other.getStartHour();
        LocalDateTime appointmentEndTime=other.getExpectedEndTime();
        return (this.startHour.isBefore(appointmentStartTime) || this.startHour.equals(appointmentStartTime))
                &&
                (this.getExpectedEndTime().isAfter(appointmentEndTime) || this.getExpectedEndTime().equals(appointmentEndTime));
    }

    public Appointment convertDST() {
        return Appointment.builder()
                .startHour(this.getStartHour().minusHours(1L))
                .expectedEndTime(this.getExpectedEndTime().minusHours(1L))
                .student(this.getStudent())
                .teacher(this.getTeacher())
                .build();

    }
}
