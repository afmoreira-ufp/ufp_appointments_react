package me.afmiguez.project.ufp_applications.appointments.presentation.controllers.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResponsesException.class})
    protected ResponseEntity<Object> handleConflict(ResponsesException ex, WebRequest request){
        HttpHeaders httpHeaders=new HttpHeaders();
        String message= Objects.requireNonNull(ex.getMessage()).substring(ex.getMessage().indexOf("\"")+1,ex.getMessage().lastIndexOf("\""));
        httpHeaders.add("STATUS",message);
        return handleExceptionInternal(ex,ex.getMessage(),httpHeaders, ex.getStatus(),request);
    }
}
