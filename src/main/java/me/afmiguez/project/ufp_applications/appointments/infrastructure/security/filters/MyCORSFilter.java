package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config.SecurityConstants.TOKEN_RESPONSE;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class MyCORSFilter implements Filter {

@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin=((HttpServletRequest) req).getHeader("Origin");

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS,PATCH");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "180");
        response.setHeader("Access-Control-Expose-Headers","Authorization,Cache-Control,Content-Type,USER_TYPE,"+TOKEN_RESPONSE+",STATUS");

    log.info(
            "Logging Request  {} : {} : {}", request.getMethod(),
            request.getRequestURI(),request.getRemoteAddr());
        chain.doFilter(req, res);


    }

}
