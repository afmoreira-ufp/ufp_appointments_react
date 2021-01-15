package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;

public interface GetCourseByName {
    Course findByName(String courseName);
}
