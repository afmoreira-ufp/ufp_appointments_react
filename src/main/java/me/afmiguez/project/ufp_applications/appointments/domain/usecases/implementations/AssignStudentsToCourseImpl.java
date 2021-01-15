package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Course;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignStudentsToCourse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AssignStudentsToCourseImpl implements AssignStudentsToCourse {

    private final CourseDAO courseDAO;
    private final StudentDAO studentDAO;

    @Override
    public Course assignStudentsToCourse(Course course, List<Student> students) {

        Optional<Course> optionalCourse=courseDAO.findByName(course.getName());
        if(optionalCourse.isPresent()){
            Course courseFromDAO=optionalCourse.get();

            courseFromDAO.getStudents().forEach(student -> student.removeCourse(courseFromDAO));
            courseFromDAO.getStudents().clear();

            List<Student> validStudent=validateStudents(students);

            validStudent.forEach(student -> {
                Optional<Student> optionalStudent=studentDAO.findByUsername(student.getUsername());
                Student studentFromDAO=optionalStudent.orElseGet(() -> Student.builder().username(student.getUsername()).build());
                courseFromDAO.addStudent(studentFromDAO);
                studentDAO.save(studentFromDAO);
            });
            return courseDAO.save(courseFromDAO);
        }
        throw new ResponsesException(HttpStatus.BAD_REQUEST,"Course does not exists");


    }

    private List<Student> validateStudents(List<Student> students) {
        List<Student> studentsFromDAO=new ArrayList<>();
        for(Student student:students){
            Optional<Student> optional=studentDAO.findByUsername(student.getUsername());
            if(optional.isPresent()){
                studentsFromDAO.add(optional.get());
            }else{
                studentsFromDAO.add(studentDAO.save(Student.builder().username(student.getUsername()).build()));
            }

        }
        return studentsFromDAO;
    }
}
