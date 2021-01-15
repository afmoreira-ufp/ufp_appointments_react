package me.afmiguez.project.ufp_applications.appointments;

import lombok.RequiredArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AuthorityDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.CourseDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.TeacherDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.*;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.ExternalTeacherService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
@Profile({"dev","prod"})
@RequiredArgsConstructor
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;
    private final ExternalTeacherService externalTeacherService;
    private final AuthorityDAO authorityDAO;

    @Transactional
    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {

        if(authorityDAO.count()==0) {
            Authority adminAuthority = authorityDAO.save(Authority.builder().role("ADMIN").build());
            Authority teacherAuthority = authorityDAO.save(Authority.builder().role("TEACHER").build());
            authorityDAO.save(Authority.builder().role("STUDENT").build());

            Arrays.asList(Course.builder().name("Engenharia de Software").build(), Course.builder().name("Redes de Computadores I").build(), Course.builder().name("Arquitetura de Computadores").build(), Course.builder().name("Análise de Sistemas").build(), Course.builder().name("Sistemas de Informação").build()).forEach(courseDAO::save);

            Teacher teacher = externalTeacherService.getTeacherFromExternal("teacher");
            teacher.addAuthority(adminAuthority);
            teacher.addAuthority(teacherAuthority);

            courseDAO.findAll().forEach(course -> {
                teacher.addCourse(course);
                teacherDAO.save(teacher);
                courseDAO.save(course);
            });
        }
    }
}
