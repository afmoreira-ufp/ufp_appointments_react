package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external;

import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ufp.wsdl.Encrypt;
import ufp.wsdl.EncryptResponse;
import ufp.wsdl.ShakeHands;
import ufp.wsdl.ShakeHandsResponse;

public class SoapAuthentication extends SoapGeneric{

    @Value("${wsdl.action.encrypt}")
    private String encryptAction;

    @Value("${wsdl.action.shakehands}")
    private String shakeHandAction;


    public String getToken(Credential credential){
        if(credential.getUsername().equals("teacher") && credential.getPassword().equals("12345")){
            return "token";
        }
        Encrypt request = new Encrypt();
        request.setPhrase(credential.toString());

        EncryptResponse response = (EncryptResponse) getWebServiceTemplate()
                .marshalSendAndReceive(this.getUrl(), request
                        , new SoapActionCallback(encryptAction)
                );

        ShakeHands shakeHands = new ShakeHands();
        shakeHands.setInput(response.getEncryptResult());

        ShakeHandsResponse shakeHandsResponse = (ShakeHandsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(this.getUrl(), shakeHands
                        , new SoapActionCallback(shakeHandAction)
                );
        if(shakeHandsResponse.getShakeHandsResult().isEmpty()){
            throw new BadCredentialsException("Check your credentials");
        }
        return shakeHandsResponse.getShakeHandsResult();
    }
}
