package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetCourseByName;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCourseByNameImpl implements GetCourseByName {

    private final CourseDAO courseDAO;

    @Override
    public Course findByName(String courseName) {
        return courseDAO.findByName(courseName).orElseThrow();
    }
}
