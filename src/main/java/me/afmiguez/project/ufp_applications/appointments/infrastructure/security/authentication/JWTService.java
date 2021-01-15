package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config.SecurityConstants.*;

@Service
public class JWTService {

    public String createJWT(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }

    public String getUser(String token){
        JWTVerifier verifier=JWT.require(HMAC512(SECRET.getBytes()))
                .build();
        try{
            return verifier.verify(token.replace(TOKEN_PREFIX,"")).getSubject();
        }catch(TokenExpiredException exc){
            return "";
        }
    }
}
