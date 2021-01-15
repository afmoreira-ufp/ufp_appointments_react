package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;

import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.GetTeacherById;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;

import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetTeacherByIdImpl implements GetTeacherById {

    private final TeacherDAO teacherDAO;

    @Override
    public Optional<Teacher> getTeacherById(Long id) {
        Optional<Teacher> optional=teacherDAO.findById(id);
        if(optional.isEmpty()) {
            throw new ResponsesException(HttpStatus.NOT_FOUND, "Does not found the Teacher");
        }
        return optional;
    }
}
