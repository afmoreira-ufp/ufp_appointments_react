package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Student extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @OneToMany(mappedBy = "student",orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "students")
    @EqualsAndHashCode.Exclude
    private List<Course> courses=new ArrayList<>();

    public void addAppointment(Appointment appointment) {
        if(getAppointments()==null) setAppointments(new ArrayList<>());
        if(!hasAppointment(appointment)){
            appointments.add(appointment);
            appointment.setStudent(this);
        }
    }

    public boolean hasAppointment(Appointment appointment) {
        return getAppointments().contains(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        if(hasAppointment(appointment)) {
            appointments.remove(appointment);
            appointment.setStudent(null);
        }
    }

    public void addCourse(Course course) {
        if(getCourses()==null)setCourses(new ArrayList<>());
        if(!this.courses.contains(course)){
            this.courses.add(course);
            course.addStudent(this);
        }
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }
}
