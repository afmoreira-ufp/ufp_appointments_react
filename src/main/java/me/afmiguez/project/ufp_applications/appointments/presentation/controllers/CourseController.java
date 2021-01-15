package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.AssociateStudentsToCourseDTO;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.CourseDTO;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.AssignStudentsToCourse;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetAllCourses;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform.ModelToDTOFactory;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetCourseByName;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final GetAllCourses getAllCourses;
    private final AssignStudentsToCourse assignStudentsToCourse;
    private final GetCourseByName getCourseByName;
    private final ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();

    @GetMapping
    public List<CourseDTO> getAllCourses(){
        return getAllCourses.getAllCourses().stream().map(modelToDTOFactory::convertCourse).collect(Collectors.toList());
    }

    @PutMapping
    public CourseDTO associateStudentsToCourse(@RequestBody AssociateStudentsToCourseDTO associateStudentsToCourseDTO){
        return modelToDTOFactory.convertCourse(
                assignStudentsToCourse.assignStudentsToCourse(
                        modelToDTOFactory.convertCourseResponseDTOToModel(associateStudentsToCourseDTO.getCourse()),
                        associateStudentsToCourseDTO.getStudents().stream().map(modelToDTOFactory::convertStudentToModel).collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/{courseName}")
    public CourseDTO getCourseByName(@PathVariable("courseName")String courseName){
        return modelToDTOFactory.convertCourse(getCourseByName.findByName(courseName));
    }

}
