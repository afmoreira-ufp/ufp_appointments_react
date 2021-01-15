package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class WSDLConfiguration {

    @Value("${wsdl.url}")
    private String url;

    @Value("${wsdl.path}")
    private String path;


    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(path);
        return marshaller;
    }

    @Bean
    public SoapAuthentication soapAuthentication(Jaxb2Marshaller marshaller){
        SoapAuthentication client = new SoapAuthentication();
        client.setDefaultUri(url);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }



    @Bean
    public ExternalTeacherService professorScheduleService(Jaxb2Marshaller marshaller) {
        ExternalTeacherService client = new ExternalTeacherService();
        client.setDefaultUri(url);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    @Bean
    public ExternalStudentService studentPerfilService(Jaxb2Marshaller marshaller) {
        ExternalStudentService client = new ExternalStudentService();
        client.setDefaultUri(url);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
