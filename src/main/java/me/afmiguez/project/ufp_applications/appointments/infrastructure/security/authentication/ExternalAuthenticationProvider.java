package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication;

import lombok.AllArgsConstructor;
import me.afmiguez.project.ufp_applications.appointments.Utils;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.AuthorityDAO;
import me.afmiguez.project.ufp_applications.appointments.persistence.interfaces.UserDAO;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractUser;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Authority;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Student;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.ExternalStudentService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.ExternalTeacherService;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.SoapAuthentication;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.models.Credential;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ExternalAuthenticationProvider implements AuthenticationProvider {

    private final SoapAuthentication soapAuthentication;
    private final UserDAO userDAO;
    private final ExternalTeacherService externalTeacherService;
    private final ExternalStudentService externalStudentService;
    private final AuthorityDAO authorityDAO;


    @Bean
    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Credential credential = new Credential(username, password);
        String token=this.soapAuthentication.getToken(credential);
        if(!token.isEmpty()){
            AbstractUser user;
            if(userDAO.contains(username)){
               user=userDAO.findByUsername(username).orElseThrow(()->new RuntimeException("Some weird thing just happened"));

               //student already exists but does not have firstname (due to be created when associate it to a course)
                if (!isTeacher(username) && (user.getFullName()==null || user.getFullName().isEmpty() || user.getFullName().isBlank())){
                    Student fromExternal=externalStudentService.getStudentFromExternal(token);
                    user.setFullName(fromExternal.getFullName());
                    user.addAuthority(authorityDAO.findByRole("STUDENT").orElseThrow(()->new RuntimeException("Error finding authority")));
                    userDAO.save(user);
                }
            }
            //user does not exist at all
            else{

                //user should be a teacher
                if (isTeacher(username)){
                    user=externalTeacherService.getTeacherFromExternal(username);
                    Authority teacherAuthority=authorityDAO.findByRole("TEACHER").orElseThrow(()->new RuntimeException("Error finding authority"));
                    if(username.contains("afmoreira")){
                        Set<Authority> authorities=new HashSet<>();
                        authorities.add((authorityDAO.findByRole("ADMIN").orElseThrow(()->new RuntimeException("Error finding authority"))));
                        authorities.add(teacherAuthority);
                        user.setAuthorities(authorities);
                    }else{
                        user.setAuthorities(Collections.singleton(teacherAuthority));
                    }
                }
                //user should be a student
                else{
                    user=externalStudentService.getStudentFromExternal(token);
                    Authority studentAuthority=authorityDAO.findByRole("STUDENT").orElseThrow(()->new RuntimeException("Error finding authority"));

                    if(username.contains("27583")){
                        Set<Authority> authorities=new HashSet<>();
                        authorities.add((authorityDAO.findByRole("ADMIN").orElseThrow(()->new RuntimeException("Error finding authority"))));
                        authorities.add(studentAuthority);
                        user.setAuthorities(authorities);
                    }else{
                        user.setAuthorities(Collections.singleton(studentAuthority));
                    }
                }
                user=userDAO.save(user);
            }
            return new UsernamePasswordAuthenticationToken(user.getUsername(), "",
                    Utils.getAuthorities(user.getAuthorities()));
        }
        throw new BadCredentialsException("External system authentication failed");
    }

    private boolean isTeacher(String username){
        return Pattern.matches("[a-z]+", username);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
