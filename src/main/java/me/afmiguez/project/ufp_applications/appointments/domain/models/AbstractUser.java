package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_details")
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @EqualsAndHashCode.Include
    private String username;
    private String fullName;
    @ManyToMany
    @JoinTable(name = "user_authority",
            joinColumns = {@JoinColumn(name="USER_ID",referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID",referencedColumnName = "ID")}
            )
    @Builder.Default
    private Set<Authority> authorities =new HashSet<>();
    @Builder.Default
    private boolean enabled =true;
    @Builder.Default
    private boolean accountNonExpired =true;
    @Builder.Default
    private boolean accountNonLocked =true;
    @Builder.Default
    private boolean credentialsNonExpired =true;

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }
}
