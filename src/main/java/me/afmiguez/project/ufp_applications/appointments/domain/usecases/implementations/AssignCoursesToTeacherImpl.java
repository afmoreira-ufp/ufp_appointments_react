package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignCoursesToTeacher;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignCoursesToTeacherImpl implements AssignCoursesToTeacher {

    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;

    public AssignCoursesToTeacherImpl(TeacherDAO teacherDAO, CourseDAO courseDAO) {
        this.teacherDAO = teacherDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public Teacher assignCourses(Long teacherId, List<Course> courseList) {
        Optional<Teacher> optionalTeacher=teacherDAO.findById(teacherId);
        optionalTeacher.orElseThrow(()->new ResponsesException(HttpStatus.BAD_REQUEST,"Teacher does not exists"));
        return assignCourses(optionalTeacher.get(),courseList);
    }

    private Teacher assignCourses(Teacher teacherFromDao, List<Course> courseList) {

        teacherFromDao.getCourses().forEach(course -> course.removeTeacher(teacherFromDao));
        teacherFromDao.getCourses().clear();

        List<Course> validCourses=validateCourse(courseList);

        validCourses.forEach(course -> {
            teacherFromDao.addCourse(course);
            courseDAO.save(course);
        });
        return teacherDAO.save(teacherFromDao);
    }

    @Override
    public Teacher assignCourses(String username, List<Course> courseList) {
        Optional<Teacher> optionalTeacher=teacherDAO.findByUsername(username);
        optionalTeacher.orElseThrow(()-> new ResponsesException(HttpStatus.BAD_REQUEST,"Teacher does not exists"));
        return assignCourses(optionalTeacher.get(),courseList);
    }

    private List<Course> validateCourse(List<Course> courseList) {
        return courseList.stream().filter(course -> courseDAO.findByName(course.getName()).isPresent()).collect(Collectors.toList());
    }
}
