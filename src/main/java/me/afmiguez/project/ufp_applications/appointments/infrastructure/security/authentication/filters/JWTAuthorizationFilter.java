package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.filters;

import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.JWTService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.UserService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.CustomUserDetailsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

import static me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config.SecurityConstants.*;

@Transactional
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final JWTService jwtService;


    public JWTAuthorizationFilter(AuthenticationManager authManager, UserService userService, JWTService jwtService) {
        super(authManager);
        this.userService=userService;
        this.jwtService=jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = req.getHeader(HEADER_STRING);
        logger.debug("is token null/empty? "+(token==null || token.isEmpty()));
        if (token == null || !token.contains(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        String username=getUser(token);
        if(!username.isEmpty()){
            UsernamePasswordAuthenticationToken authentication = getAuthentication(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            res.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            res.setHeader(TOKEN_RESPONSE,"EXPIRED");
        }


        chain.doFilter(req, res);
    }

    private String getUser(String token){
        logger.debug("Try to process authorization");
        if (token != null) {
            return jwtService.getUser(token);
        }
        return "";
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String username) {
        CustomUserDetailsUser userDetails=this.userService.loadUserByUsername(username);
        if(userDetails!=null) {
            logger.debug("User found "+username);
            logger.debug(userDetails.getAuthorities().toString());
            return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                    userDetails.getAuthorities());
        }
        throw new UsernameNotFoundException("");
    }
}
