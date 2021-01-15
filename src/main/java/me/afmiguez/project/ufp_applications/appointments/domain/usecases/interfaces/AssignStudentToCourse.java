package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;

public interface AssignStudentToCourse {
    Student assignStudentToCourse(Student student, Course course);
}
