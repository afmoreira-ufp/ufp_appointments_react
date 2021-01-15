package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@DiscriminatorValue("regularSchedule")
@SuperBuilder
@NoArgsConstructor
public class RegularSchedule extends AbstractSchedule{


}
