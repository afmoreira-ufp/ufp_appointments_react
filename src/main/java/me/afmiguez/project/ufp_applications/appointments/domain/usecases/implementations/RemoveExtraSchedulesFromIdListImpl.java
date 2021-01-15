package me.afmiguez.project.ufp_applications.appointments.domain.usecases.implementations;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions.ResponsesException;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractSchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.RemoveExtraSchedulesFromIdList;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemoveExtraSchedulesFromIdListImpl implements RemoveExtraSchedulesFromIdList {

    private final TeacherDAO teacherDAO;

    @Override
    public Teacher removeExtraSchedules(String username, List<Long> ids) {
        Optional<Teacher> optional=teacherDAO.findByUsername(username);
        if(optional.isEmpty()){
            throw new ResponsesException(HttpStatus.NOT_FOUND,"Teacher does not exist");
        }
        Teacher teacher=optional.get();
        List<AbstractSchedule> schedules=teacher.getSchedules();
        List<AbstractSchedule> schedulesToBeRemoved=schedules.stream().filter(schedule -> ids.contains(schedule.getId())).collect(Collectors.toList());
        schedulesToBeRemoved.forEach(teacher::removeSchedule);
        return teacherDAO.save(teacher);
    }
}
