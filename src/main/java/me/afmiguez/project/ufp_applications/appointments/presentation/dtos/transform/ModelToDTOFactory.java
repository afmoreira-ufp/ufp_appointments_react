package me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform;

import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.*;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.externalCalendar.ClientDTO;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.externalCalendar.EmployeeDTO;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.externalCalendar.ExternalAppointmentDTO;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModelToDTOFactory {

    private static ModelToDTOFactory instance;

    private ModelToDTOFactory(){}

    public static ModelToDTOFactory getInstance(){
        if(instance==null){
            instance=new ModelToDTOFactory();
        }
        return instance;
    }

    public ExternalAppointmentDTO convertToExternalAppointment(Appointment appointment){
        return ExternalAppointmentDTO.builder()
                .startTime(appointment.getStartHour())
                .expectedEndTime(appointment.getExpectedEndTime())
                .employee(EmployeeDTO.builder()
                        .username(appointment.getTeacher().getUsername())
                        .build())
                .client(ClientDTO.builder()
                        .clientName(appointment.getStudent().getFullName())
                        .username(appointment.getStudent().getUsername())
                        .build())
                .build();
    }

    public Appointment convertToAppointment(ExternalAppointmentDTO appointmentDTO){
        return Appointment.builder()
                .startHour(appointmentDTO.getStartTime())
                .expectedEndTime(appointmentDTO.getExpectedEndTime())
                .student(Student.builder()
                        .fullName(appointmentDTO.getClient().getClientName())
                        .username(appointmentDTO.getClient().getUsername())
                        .build())
                .teacher(Teacher.builder()
                        .username(appointmentDTO.getEmployee().getUsername())
                        .build())
                .build();
    }

    public TeacherResponseDTO convertTeacherToResponse(Teacher teacher){
        return TeacherResponseDTO.builder()
                .id(teacher.getId())
                .username(teacher.getUsername())
                .appointments(teacher.getAppointments()!=null?teacher.getAppointments().stream().map(this::convertAppointment).collect(Collectors.toList()):new ArrayList<>())
                .courses(teacher.getCourses()!=null?teacher.getCourses().stream().map(this::convertCourse).collect(Collectors.toList()):new ArrayList<>())
                .schedules(teacher.getSchedules()!=null?teacher.getSchedules().stream().map(this::convertSchedule).collect(Collectors.toList()):new ArrayList<>())
                .build();
    }

    private ScheduleDTO convertSchedule(AbstractSchedule abstractSchedule) {
        if(abstractSchedule instanceof RegularSchedule) {
            return RegularScheduleDTO.builder()
                    .id(abstractSchedule.getId())
                    .dayOfWeek(abstractSchedule.getDayOfWeek())
                    .startTime(abstractSchedule.getStartTime())
                    .endTime(abstractSchedule.getEndTime())
                    .build();
        }else{
            ExtraordinarySchedule extraordinarySchedule=(ExtraordinarySchedule)abstractSchedule;
            return ExtraScheduleDTO.builder()
                    .id(extraordinarySchedule.getId())
                    .startDate(extraordinarySchedule.getStartPeriod())
                    .endDate(extraordinarySchedule.getEndPeriod())
                    .dayOfWeek(extraordinarySchedule.getDayOfWeek())
                    .startTime(extraordinarySchedule.getStartTime())
                    .endTime(extraordinarySchedule.getEndTime())
                    .courseDTO(convertCourse(extraordinarySchedule.getCourse()))
                    .build();
        }
    }

    public AppointmentDTO convertAppointment(Appointment appointment) {
        return appointment!=null?AppointmentDTO.builder()
                .id(appointment.getId())
                .studentUser(appointment.getStudent()!=null?appointment.getStudent().getUsername():null)
                .teacherUser(appointment.getTeacher()!=null?appointment.getTeacher().getUsername():null)
                .startHour(appointment.getStartHour())
                .expectedEndHour(appointment.getExpectedEndTime()!=null?appointment.getExpectedEndTime():appointment.getStartHour().plusMinutes(30))
                .build():null;
    }

    public CourseDTO convertCourse(Course course){

            return CourseDTO.builder()
                    .id(course.getId() != null ? course.getId() : null)
                    .name(course.getName())
                    .students(course.getStudents().stream().map(AbstractUser::getUsername).collect(Collectors.toList()))
                    .build();

    }


    public AbstractSchedule convertScheduleToModel(ScheduleDTO scheduleDTO) {
        if(scheduleDTO instanceof ExtraScheduleDTO){
            ExtraScheduleDTO extraScheduleDTO=(ExtraScheduleDTO)scheduleDTO;
            return ExtraordinarySchedule.builder()
                    .dayOfWeek(extraScheduleDTO.getDayOfWeek())
                    .startTime(extraScheduleDTO.getStartTime())
                    .endTime(extraScheduleDTO.getEndTime())
                    .startPeriod(extraScheduleDTO.getStartDate())
                    .endPeriod(extraScheduleDTO.getEndDate())
                    .course(convertCourseDTOToModel(extraScheduleDTO.getCourseDTO()))
                    .build();

        }
        RegularScheduleDTO regularScheduleDTO=(RegularScheduleDTO)scheduleDTO;
        return RegularSchedule.builder()
                .dayOfWeek(regularScheduleDTO.getDayOfWeek())
                .startTime(regularScheduleDTO.getStartTime())
                .endTime(regularScheduleDTO.getEndTime())
                .build();
    }

    public Course convertCourseDTOToModel(CourseDTO courseDTO){
        return Course.builder()
                .id(courseDTO.getId())
                .name(courseDTO.getName())
                .build();
    }

    public Course convertCourseResponseDTOToModel(CourseResponseDTO courseDTO){
        return Course.builder()
                .name(courseDTO.getName())
                .build();
    }

    public Appointment convertAppointmentToModel(AppointmentCreateDTO createDTO) {
        return Appointment.builder()
                .teacher(Teacher.builder().username(createDTO.getTeacherUsername()).build())
                .student(Student.builder().username(createDTO.getStudentUsername()).build())
                .startHour(createDTO.getStartHour())
                .build();
    }

    public Student convertStudentToModel(StudentDTO studentDTO) {
        return Student.builder()
                .username(studentDTO.getUsername())
                .build();
    }

    public AppointmentDetailsDTO convertToDetailedAppointments(Appointment appointment) {
        return AppointmentDetailsDTO.builder()
                .id(appointment.getId())
                .studentName(appointment.getStudent().getFullName())
                .studentNumber(appointment.getStudent().getUsername())
                .date(appointment.getStartHour().toLocalDate())
                .day(appointment.getStartHour().getDayOfWeek())
                .time(appointment.getStartHour().toLocalTime())
                .build();
    }

    public StudentDTO convertStudentToDTO(Student student) {
        return StudentDTO.builder()
                .username(student.getUsername())
                .build();
    }
}
