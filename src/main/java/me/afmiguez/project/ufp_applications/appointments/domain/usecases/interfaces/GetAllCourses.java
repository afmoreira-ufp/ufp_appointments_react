package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;

import java.util.List;

public interface GetAllCourses {
    List<Course> getAllCourses();
}
