package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.*;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.*;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform.ModelToDTOFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final SearchTeachers searchTeachers;
    private final InsertScheduleToTeacher insertScheduleToTeacher;
    private final ListAppointments listAppointments;
    private final AssignCoursesToTeacher assignCoursesToTeacher;
    private final GetTeacherById getTeacherById;
    private final GetTeacherByUsername getTeacherByUsername;
    private final ListTeacherAppointmentsFromNow listTeacherAppointmentsFromNow;
    private final RemoveExtraSchedulesFromIdList removeExtraSchedulesFromIdList;

    private final ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();



    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping()
    public List<TeacherResponseDTO> getAllTeachers(){
        return searchTeachers.findAllTeachers().stream().map(modelToDTOFactory::convertTeacherToResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PatchMapping(value = "/{id}/regular")
    public TeacherResponseDTO insertSchedule(@PathVariable("id")Long id, @RequestBody List<ScheduleDTO> scheduleDTOS){

            return modelToDTOFactory.convertTeacherToResponse(
                    insertScheduleToTeacher.insertSchedule(id,
                            scheduleDTOS.stream().map(
                                    modelToDTOFactory::convertScheduleToModel
                            ).collect(Collectors.toList()))
            );

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @GetMapping(value = "/{id}")
    public TeacherResponseDTO getTeacherById(@PathVariable("id")Long id){

        return modelToDTOFactory.convertTeacherToResponse(Objects.requireNonNull(getTeacherById.getTeacherById(id).orElse(null)));

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @GetMapping(value = "/{id}/course")
    public List<CourseDTO> getTeacherCourses(@PathVariable("id")Long id){

        Optional<Teacher> optional=getTeacherById.getTeacherById(id);
        return optional.map(teacher -> teacher.getCourses().stream().map(modelToDTOFactory::convertCourse).collect(Collectors.toList())).orElse(null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PatchMapping(value = "/{id}/extraordinary")
    public TeacherResponseDTO insertExtraordinarySchedule(@PathVariable("id")Long id, @RequestBody List<ExtraScheduleDTO> scheduleDTOS){

        return modelToDTOFactory.convertTeacherToResponse(
                insertScheduleToTeacher.insertSchedule(id,
                        scheduleDTOS.stream().map(
                                modelToDTOFactory::convertScheduleToModel
                        ).collect(Collectors.toList()))
        );

    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/{id}/appointment")
    public List<AppointmentDTO> listAppointments(@PathVariable("id")Long id){
        return listAppointments.listAppointments(id).stream().map(modelToDTOFactory::convertAppointment).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @GetMapping("/{username}/nextAppointments")
    public List<AppointmentDetailsDTO> listNextAppointments(@PathVariable("username")String username){
        List<Appointment> appointments=listTeacherAppointmentsFromNow.listAppointmentsFromNow(username);
        return appointments.stream().map(modelToDTOFactory::convertToDetailedAppointments).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping(value = "/{id}/course")
    public TeacherResponseDTO insertCourseToTeacher(@PathVariable("id")Long id,@RequestBody List<CourseDTO> courses){
        return modelToDTOFactory.convertTeacherToResponse(assignCoursesToTeacher.assignCourses(
                id,
                courses.stream().map(modelToDTOFactory::convertCourseDTOToModel).collect(Collectors.toList())
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping(value = "/name/{username}/course")
    public TeacherResponseDTO insertCourseToTeacherByUsername(@PathVariable("username")String username,@RequestBody List<CourseDTO> courses){
        return modelToDTOFactory.convertTeacherToResponse(assignCoursesToTeacher.assignCourses(
                username,
                courses.stream().map(modelToDTOFactory::convertCourseDTOToModel).collect(Collectors.toList())
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @GetMapping(value="/name/{username}")
    public TeacherResponseDTO getTeacherByUsername(@PathVariable("username")String username){
        Teacher teacher=getTeacherByUsername.getTeacherByUsername(username);
        return modelToDTOFactory.convertTeacherToResponse(teacher);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping(value="/name/{username}/removeExtra")
    public TeacherResponseDTO removeExtraSchedulesFromIdList(@PathVariable("username") String username, @RequestBody List<Long> ids){
        System.out.println(username);
        System.out.println(ids);
        return modelToDTOFactory.convertTeacherToResponse(removeExtraSchedulesFromIdList.removeExtraSchedules(username,ids));
    }

}
