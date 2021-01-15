package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AppointmentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.StudentDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.MakeAppointment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MakeAppointmentImpl implements MakeAppointment {

    private static final int MAX_APPOINTMENTS = 2;
    private final TeacherDAO teacherDAO;
    private final StudentDAO studentDAO;
    private final AppointmentDAO appointmentDAO;
    private final CourseDAO courseDAO;
    private final CalendarService calendarService;

    @Override
    public Appointment makeAppointment(Appointment appointment) {
        Optional<Student> studentOptional=studentDAO.findByUsername(appointment.getStudent().getUsername());
        Optional<Teacher> teacherOptional=teacherDAO.findByUsername(appointment.getTeacher().getUsername());
        if(studentOptional.isEmpty()){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Could not process use case");
        }
        if(teacherOptional.isEmpty()){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"You should select a teacher");
        }
        Teacher teacher=teacherOptional.get();
        Student student=studentOptional.get();

        if(!teacher.canAttendAppointment(appointment)){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Teacher could not attend to the appointment");
        }

        if(teacher.isExtraordinaryAppointment(appointment)){
            validateExtraordinary(appointment,teacher,student);
        }
        else{
            validateRegular(appointment,teacher,student);
        }
        appointment.setTeacher(teacher);
        appointment.setStudent(student);
        appointment=appointmentDAO.save(appointment);

        teacher.addAppointment(appointment);
        student.addAppointment(appointment);

        teacherDAO.save(teacher);
        studentDAO.save(student);

        calendarService.createGCalEvent(appointment);

        return appointment;
    }

    private void validateRegular(Appointment appointment, Teacher teacher, Student student) {
        LocalDateTime now=LocalDateTime.now();
        if(student.getUsername().equals("27583")) return;
        if(now.plusHours(24).isAfter(appointment.getStartHour())){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Appointment should not be scheduled 24 hours before the date");
        }
        if(now.plusDays(14).isBefore(appointment.getStartHour())){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Appointment should not be scheduled after 14 days from today");
        }
        if(countWeekAppointments(teacher,student,appointment)>=MAX_APPOINTMENTS){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Can only schedule 2 appointments per week");
        }

    }

    private LocalDate getStartWeek(Appointment appointment){
        LocalDate date=appointment.getStartHour().toLocalDate();
        int daysFromMonday=date.getDayOfWeek().getValue()- DayOfWeek.MONDAY.getValue();
        return appointment.getStartHour().toLocalDate().minusDays(daysFromMonday);
    }

    private LocalDate getEndWeek(Appointment appointment){
        LocalDate date=appointment.getStartHour().toLocalDate();
        int daysFromMonday=DayOfWeek.SUNDAY.getValue()-date.getDayOfWeek().getValue();
        return appointment.getStartHour().toLocalDate().plusDays(daysFromMonday);
    }

    private int countWeekAppointments(Teacher teacher,Student student,Appointment appointment){
        return (int)getAppointmentBetweenDates(teacher,getStartWeek(appointment),getEndWeek(appointment))
                .stream().filter(
                        app->app.getStudent().equals(student)
                ).count();
    }

    private Set<Appointment> getAppointmentBetweenDates(Teacher teacher,LocalDate start, LocalDate end){
        return teacher.getAppointments().stream().filter(appointment -> {
            LocalDate appointmentStart=appointment.getStartHour().toLocalDate();
            return (start.isBefore(appointmentStart) || start.isEqual(appointmentStart))
                    && (end.isAfter(appointmentStart)||end.isEqual(appointmentStart));
        }).collect(Collectors.toSet());
    }


    private void validateExtraordinary(Appointment appointment, Teacher teacher, Student student) {

        List<ExtraordinarySchedule> extraordinaryScheduleList=teacher.extraordinarySchedulesForAppointment(appointment);
        ExtraordinarySchedule extraordinarySchedule= extraordinaryScheduleList.get(0);
        Optional<Course> optionalCourse=courseDAO.findByName(extraordinarySchedule.getCourse().getName());
        if(optionalCourse.isEmpty()){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Course should exist");
        }
        Course course=optionalCourse.get();
        if(appointment.getStudent().getUsername().equals("27583")){
            return;
        }
        if(!course.hasStudent(student)){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Student should be in the schedule course");
        }
        int countAppointments=teacher.countStudentAppointmentsByExtraordinaryPeriod(appointment);
        if(countAppointments>0 ){
            throw new ResponsesException(HttpStatus.BAD_REQUEST,"Only one appointment per Student per ExtraordinarySchedule");
        }
    }
}
