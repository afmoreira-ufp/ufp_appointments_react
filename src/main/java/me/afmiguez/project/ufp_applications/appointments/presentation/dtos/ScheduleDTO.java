package me.afmiguez.project.ufp_applications.appointments.presentation.dtos;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = RegularScheduleDTO.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularScheduleDTO.class, name = "standard"),
        @JsonSubTypes.Type(value = ExtraScheduleDTO.class, name = "other")
})
public interface ScheduleDTO {

}
