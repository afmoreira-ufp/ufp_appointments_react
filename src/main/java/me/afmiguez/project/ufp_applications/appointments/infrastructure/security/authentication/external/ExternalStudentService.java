package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external;

import me.afmiguez.project.ufp_applications.appointments.Utils;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models.StudentJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ufp.wsdl.Perfil;
import ufp.wsdl.PerfilResponse;

import java.io.IOException;

public class ExternalStudentService extends SoapGeneric{

    @Value("${wsdl.action.perfil}")
    private String perfilAction;


    public StudentJSON getPerfilLanguage(String token){
        Perfil perfil=new Perfil();
        perfil.setToken(token);
        PerfilResponse response=(PerfilResponse) getWebServiceTemplate()
                .marshalSendAndReceive(this.getUrl(),perfil,new SoapActionCallback(perfilAction));
        if(response.getPerfilResult().contains("Error")){
            throw new RuntimeException("Check your credentials");
        }
        try{
            return Utils.getValue(response.getPerfilResult(), StudentJSON.class);
        }catch(IOException ioe){
            throw new RuntimeException("Error at getting student profile");
        }
    }

    public Student getStudentFromExternal(String token) {
        StudentJSON studentJSON=getPerfilLanguage(token);
        return Student.builder()
                .username(studentJSON.getNumero())
                .fullName(studentJSON.getNome())
                .build();
    }

}
