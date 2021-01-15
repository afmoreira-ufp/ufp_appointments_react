package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "authorities")
    @Builder.Default
    private Set<AbstractUser> abstractUsers =new HashSet<>();

    @Column(unique = true)
    private String role;


}