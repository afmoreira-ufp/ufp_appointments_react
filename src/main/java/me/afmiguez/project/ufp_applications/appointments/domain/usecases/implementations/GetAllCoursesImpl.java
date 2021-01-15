package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetAllCourses;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCoursesImpl implements GetAllCourses {

    private final CourseDAO courseDAO;

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
}
