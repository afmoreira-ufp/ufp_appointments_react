package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.config;

import lombok.AllArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.*;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.filters.JWTAuthenticationFilter;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.filters.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//excluded from tests
@Profile({"prod","dev"})
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JWTService jwtService;
    private final ExternalAuthenticationProvider externalAuthenticationProvider;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080","http://localhost:3000","https://afmiguez.me","https://localhost:8090");
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors();

        http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
        http.authorizeRequests()
                .antMatchers( "/login").permitAll()
                .antMatchers("/h2-console/**","/*","/login","/api/classes/**","/js/**","/css/**","/appointments","/static/**","/index*","/built/**" ,"/rco2").permitAll()


                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(),userService,jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService, this.jwtService))

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().and().csrf().disable();


        http.headers().frameOptions().sameOrigin();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.externalAuthenticationProvider);
    }



}
