package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.afmiguez.project.ufp_applications.appointments.Utils;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.JWTService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.UserService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.Credential;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.CustomUserDetailsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import static me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config.SecurityConstants.HEADER_STRING;
import static me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config.SecurityConstants.TOKEN_PREFIX;

@Transactional
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            StringBuilder sb = new StringBuilder();
            req.getReader().lines().forEach(sb::append);
            Credential credential = Utils.getValue(sb.toString(),Credential.class);
            String username = credential.getUsername();
            String password = credential.getPassword();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

            // Allow subclasses to set the "details" property
            setDetails(req, authRequest);

            return this.authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        logger.debug("principal " + auth.getPrincipal().toString());

        String username = (String) auth.getPrincipal();
        logger.debug("principal " + username);
        CustomUserDetailsUser userDetails = this.userService.loadUserByUsername(username);
        if (userDetails==null) {
            AbstractUser abstractUser = new AbstractUser();
            abstractUser.setUsername(username);
        }

        String token = jwtService.createJWT(username);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        if (Pattern.matches("[a-z]+", username)) {
            res.addHeader("USER_TYPE", "teacher");
        } else {
            res.addHeader("USER_TYPE", "student");
        }
        try {
            PrintWriter out= res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            ObjectMapper mapper=new ObjectMapper();
            System.out.println(mapper.writeValueAsString(userDetails));
            out.print(mapper.writeValueAsString(userDetails));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
