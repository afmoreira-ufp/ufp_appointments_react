package me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponsesException extends ResponseStatusException {

    public ResponsesException(HttpStatus status, String reason) {
        super(status, reason);
    }

}
