package me.afmiguez.project.ufp_applications.appointments.domain.models;

public interface Schedule {
    boolean contains(Appointment appointment);

    void setTeacher(Teacher teacher);
}
