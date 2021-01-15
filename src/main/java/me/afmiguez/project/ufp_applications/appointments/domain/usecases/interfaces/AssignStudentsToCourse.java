package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;

import java.util.List;

public interface AssignStudentsToCourse {
    Course assignStudentsToCourse(Course course, List<Student> students);
}
