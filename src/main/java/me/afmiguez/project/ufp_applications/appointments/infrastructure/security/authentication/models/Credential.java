package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Credential {
    private String username;
    private String password;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString(){
        return username+","+password;
    }

}