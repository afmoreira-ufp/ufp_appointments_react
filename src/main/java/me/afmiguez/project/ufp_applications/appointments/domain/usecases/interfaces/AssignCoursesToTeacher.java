package me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;

import java.util.List;

public interface AssignCoursesToTeacher {
    Teacher assignCourses(Long teacherId, List<Course> courseList);
    Teacher assignCourses(String username, List<Course> courseList);
}
