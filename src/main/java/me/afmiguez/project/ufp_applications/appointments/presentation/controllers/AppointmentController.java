package me.afmiguez.project.ufp_applications.appointments.presentation.controllers;

import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.AppointmentCreateDTO;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.AppointmentDTO;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CancelAppointment;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.MakeAppointment;
import me.afmiguez.project.ufp_applications.appointments.presentation.dtos.transform.ModelToDTOFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(value = "localhost:3000")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final MakeAppointment makeAppointment;
    private final CancelAppointment cancelAppointment;
    private final ModelToDTOFactory modelToDTOFactory=ModelToDTOFactory.getInstance();

    public AppointmentController(MakeAppointment makeAppointment, CancelAppointment cancelAppointment) {
        this.makeAppointment = makeAppointment;
        this.cancelAppointment = cancelAppointment;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @PostMapping
    public AppointmentDTO createAppointment(@RequestBody AppointmentCreateDTO createDTO){
        return modelToDTOFactory.convertAppointment(
                makeAppointment.makeAppointment(modelToDTOFactory.convertAppointmentToModel(createDTO))
        );
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @DeleteMapping("/{id}")
    public AppointmentDTO cancelAppointment(@PathVariable("id")Long id, Principal principal){
        return modelToDTOFactory.convertAppointment(cancelAppointment.cancelAppointment(id,principal!=null?principal.getName():""));
    }

}
