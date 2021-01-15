package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractSchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.ExtraordinarySchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.InsertScheduleToTeacher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsertScheduleToTeacherImpl implements InsertScheduleToTeacher {

    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;

    @Override
    public Teacher insertSchedule(Long id, List<AbstractSchedule> schedules) throws ResponsesException {
        Optional<Teacher> optionalTeacher=teacherDAO.findById(id);
        if(optionalTeacher.isPresent()){
            Teacher teacherFromDb=optionalTeacher.get();

            schedules.forEach(
                    schedule-> {
                        if (schedule instanceof ExtraordinarySchedule) {
                            if(schedule.isValid()) {
                                Optional<Course> optionalCourse = courseDAO.findByName(((ExtraordinarySchedule) schedule).getCourse().getName());
                                optionalCourse.ifPresent(((ExtraordinarySchedule) schedule)::setCourse);
                            }
                        }
                        teacherFromDb.addSchedule(schedule);
                    });
            return teacherDAO.save(teacherFromDb);
        }
        throw new ResponsesException(HttpStatus.BAD_REQUEST,"Could not process use case");
    }
}
