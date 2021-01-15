package me.afmiguez.project.ufp_applications.appointments;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Authority;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static <E> ResponseEntity<E> makeRequest(String path, HttpMethod method, Object body, HttpHeaders headers,
                                                    Class<E> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(path, method, new HttpEntity<>(body, headers), responseType);
    }

    @SuppressWarnings({"rawtypes" })
    public static <T> T getValue(InputStream inputStream, Class<? extends Collection> collection, Class<?> classz)
            throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionLikeType(collection,
                classz);
        return objectMapper.readValue(inputStream,type);
    }

    public static <T> T getValue(String inputStream, Class<T> classz)
            throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(inputStream,classz);
    }

    public static Set<GrantedAuthority> getAuthorities(Set<Authority> authorities) {
        if(authorities!=null && authorities.size()>0) {
            return authorities.stream()
                    .map(Authority::getRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

}