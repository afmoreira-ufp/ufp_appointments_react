package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Teacher extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "teacher",orphanRemoval = true)
    private List<Appointment> appointments=new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "teacher",orphanRemoval = true)
    private List<AbstractSchedule> schedules=new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses=new ArrayList<>();

    public Teacher addAppointment(Appointment appointment) {
        if(appointments==null) appointments=new ArrayList<>();
        if(!this.appointments.contains(appointment)) {
            this.appointments.add(appointment);
            appointment.setTeacher(this);
        }
        return this;
    }

    public Teacher addSchedule(AbstractSchedule schedule){
        if(getSchedules()==null) setSchedules(new ArrayList<>());
        if(!this.getSchedules().contains(schedule)){
            schedules.add(schedule);
            schedule.setTeacher(this);
        }
        return this;
    }

    public List<ExtraordinarySchedule> getAllExtraordinarySchedules(){
        List<ExtraordinarySchedule> schedules=new ArrayList<>();
        for(AbstractSchedule abstractSchedule:this.schedules){
            if(abstractSchedule instanceof ExtraordinarySchedule){
                schedules.add((ExtraordinarySchedule) abstractSchedule);
            }
        }
        return schedules;
    }

    public List<ExtraordinarySchedule> extraordinarySchedulesForAppointment(Appointment appointment){
        return getAllExtraordinarySchedules().stream().filter(extraordinarySchedule -> (extraordinarySchedule.containsPeriod(appointment))).collect(Collectors.toList());
    }

    public int countStudentAppointmentsByExtraordinaryPeriod(Appointment appointment){
        List<ExtraordinarySchedule> extraordinarySchedules=extraordinarySchedulesForAppointment(appointment);
        int sum=0;
        for(ExtraordinarySchedule extraordinarySchedule:extraordinarySchedules){
            sum+=countStudentAppointmentsByExtraordinaryPeriod(extraordinarySchedule,appointment.getStudent());
        }
        return sum;
    }

    public int countStudentAppointmentsByExtraordinaryPeriod(ExtraordinarySchedule extraordinarySchedule,Student student){
        int count=0;
        for(Appointment appointment:getAppointmentsForExtraordinaryPeriod(extraordinarySchedule)){
            if(appointment.getStudent().equals(student)){
                count++;
            }
        }
        return count;
    }

    public boolean isExtraordinaryAppointment(Appointment appointment) {
        return getAllExtraordinarySchedules().stream().anyMatch(extraordinarySchedule -> extraordinarySchedule.contains(appointment));
    }

    public List<Appointment> getAppointmentsForExtraordinaryPeriod(ExtraordinarySchedule extraordinarySchedule){
        List<Appointment> appointments=getAppointments().stream().filter(extraordinarySchedule::contains).collect(Collectors.toList());
        return appointments;
    }

    public boolean canAttendAppointment(Appointment appointment) {
        return this.canSchedule(appointment) && this.isAvailable(appointment);
    }

    private boolean isAvailable(Appointment appointment) {
        if(getAppointments()==null)setAppointments(new ArrayList<>());
        for (Appointment app : this.getAppointments()) {
            if (app.overlaps(appointment)) {
                return false;
            }
        }
        return true;
    }

    private boolean canSchedule(Appointment appointment) {
        for (Schedule schedule : getSchedules()) {
            if (schedule.contains(appointment)) {
                return true;
            }
        }
        return false;
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setTeacher(null);
    }

    public void addCourse(Course course) {
        if(getCourses()==null)setCourses(new ArrayList<>());
        if(!this.courses.contains(course)){
            this.courses.add(course);
            course.addTeacher(this);
        }
    }

    public void removeSchedule(AbstractSchedule schedule) {
        schedules.remove(schedule);
        schedule.setTeacher(null);
    }
}
