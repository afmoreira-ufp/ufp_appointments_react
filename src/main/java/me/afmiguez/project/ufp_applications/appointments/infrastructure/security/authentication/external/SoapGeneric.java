package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapGeneric extends WebServiceGatewaySupport {

    @Value("${wsdl.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
